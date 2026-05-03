package com.dstz.sys.sdk;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.thread.GlobalThreadPool;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.dstz.base.common.property.PropertyEnum;
import com.dstz.base.common.utils.CastUtils;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.sys.rest.model.dto.AutoTranslateDTO;
import com.dstz.sys.rest.model.vo.AutoTranslateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 百度翻译客户端
 *
 */
public class BaiduFanYiClient {

	private static final Logger logger = LoggerFactory.getLogger(BaiduFanYiClient.class);

	private static final String TRANS_API = "https://fanyi-api.baidu.com/api/trans/vip/translate";

	/**
	 * 控制并发数100
	 */
	private static final Semaphore SEMAPHORE = new Semaphore(100);


	/**
	 * 翻译多语言
	 *
	 * @param translateDTO translateDTO
	 * @return 翻译结果
	 */
	public static List<AutoTranslateVO> translateMulti(AutoTranslateDTO translateDTO) throws InterruptedException {
		if (CollUtil.isEmpty(translateDTO.getToLanguages())) {
			return new ArrayList<>();
		}

		// 获取所用到参数
		final String appId = PropertyEnum.BAIDU_SDK_APPID.getPropertyValue(String.class);
		Assert.notBlank(appId, "系统属性[baidu.sdk.app-id]未配置");
		final String secret = PropertyEnum.BAIDU_SDK_SECRET.getPropertyValue(String.class);
		Assert.notBlank(secret, "系统属性[baidu.sdk.secret]未配置");

		List<AutoTranslateVO> translateVoList = new ArrayList<>(translateDTO.getToLanguages().size());
		CountDownLatch countDownLatch = new CountDownLatch(translateDTO.getToLanguages().size());

		for (String toLanguage : translateDTO.getToLanguages()) {
			AutoTranslateVO autoTranslateVO = new AutoTranslateVO();
			autoTranslateVO.setLanguage(toLanguage);
			// 控制并发100
			SEMAPHORE.acquire();
			GlobalThreadPool.execute(() -> {
				try {
					String dstText = invokeApi(autoTranslateVO.getLanguage(), buildParams(appId, secret, translateDTO.getChineseText(), autoTranslateVO.getLanguage()));
					autoTranslateVO.setDstText(dstText);
				} catch (FanYiException e) {
					autoTranslateVO.setErrorMsg(e.getMessage());
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
					autoTranslateVO.setErrorMsg("翻译失败，请查看后台日志");
				} finally {
					SEMAPHORE.release();
					countDownLatch.countDown();
				}
			});
			translateVoList.add(autoTranslateVO);
		}
		countDownLatch.await();
		return translateVoList;
	}


	public String translate(String chineseText, String toLanguage) {
		// 获取所用到参数
		final String appId = PropertyEnum.BAIDU_SDK_APPID.getYamlValue(String.class);
		Assert.notBlank(appId, "系统属性[baidu.sdk.app-id]未配置");
		final String secret = PropertyEnum.BAIDU_SDK_SECRET.getYamlValue(String.class);
		Assert.notBlank(secret, "系统属性[baidu.sdk.secret]未配置");

		Map<String, Object> params = buildParams(appId, secret, chineseText, toLanguage);

		return invokeApi(toLanguage, params);
	}

	/**
	 * 调用接口
	 *
	 * @param params 接口参数
	 * @return 翻译内容
	 */
	private static String invokeApi(String toLanguage, Map<String, Object> params) {
		StopWatch stopWatch = null;
		if (logger.isInfoEnabled()) {
			stopWatch = new StopWatch();
			stopWatch.start();
			logger.info("调用百度翻译请求参数：{}", JsonUtils.toJSONString(params));
		}
		Map<String, Object> responseMap;
		try (HttpResponse response = HttpUtil.createPost(TRANS_API)
				.contentType("application/x-www-form-urlencoded;charset=UTF-8")
				.form(params)
				.timeout(100000)
				.executeAsync()) {
			String bodyString = response.body();
			if(logger.isInfoEnabled()) {
				stopWatch.stop();
				logger.info("调用百度翻译请求响应, 耗时：{}, stateCode: {}, body：{}", stopWatch.shortSummary(TimeUnit.MILLISECONDS), response.getStatus(), bodyString);
			}
			Assert.isTrue(response.getStatus() == 200, "百度翻译服务未正常返回数据, 状态码：{}, 响应数据：{}", response.getStatus(), bodyString);
			responseMap = CastUtils.cast(JsonUtils.parseObject(bodyString, Map.class));
		}
		if (responseMap == null || responseMap.isEmpty()) {
			throw new IllegalStateException("百度翻译服务未正常返回数据！");
		}
		handleErrorCode(toLanguage, MapUtil.getStr(responseMap, "error_code"), MapUtil.getStr(responseMap, "error_msg"));
		Map<String, String> transResult = CollUtil.getFirst(CastUtils.<List<Map<String, String>>>cast(responseMap.get("trans_result")));
		return transResult.get("dst");
	}

	private static void handleErrorCode(String toLanguage, String errorCode, String errorMsg) {
		// 为空或者为52000表示成功
		if (CharSequenceUtil.isEmpty(errorCode) || CharSequenceUtil.equals(errorCode, "52000")) {
			return;
		}
		// 访问频率受限
		if (CharSequenceUtil.equals(errorCode, "54003")) {
			throw new FanYiException("调用频率过高，请稍后再试");
		}
		// 译文语言方向不支持
		if (CharSequenceUtil.equals(errorCode, "58001")) {
			throw new FanYiException(String.format("不支持语言翻译：%s", toLanguage));
		}
		throw new IllegalStateException(String.format("调用百度翻译出错, 错误码：%s, 错误信息：%s", errorCode, errorMsg));
	}

	private static String convertLanguage(String language) {
		if (CharSequenceUtil.equalsAnyIgnoreCase(language, "zh-CHT", "zh-TW")) {
			// 简体中文
			return "cht";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "en-")) {
			// 英语
			return "en";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "de-")) {
			// 德语
			return "de";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "ja-")) {
			// 日语
			return "jp";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "fr-")) {
			// 法语
			return "fra";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "ru-")) {
			// 俄语
			return "ru";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "pt-")) {
			// 葡萄牙语
			return "pt";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "es-")) {
			// 西班牙语
			return "spa";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "ar-")) {
			// 阿拉伯语
			return "ara";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "nl-")) {
			// 荷兰语
			return "nl";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "th-")) {
			// 泰语
			return "th";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "vi-")) {
			// 越南语
			return "vie";
		}
		logger.warn("无法将语言[{}]映射到百度翻译语言", language);
		throw new FanYiException(String.format("不支持翻译语言：%s", language));
	}

	private static Map<String, Object> buildParams(String appId, String secret, String chineseText, String toLanguage) {
		final String salt = Long.toString(System.currentTimeMillis());

		Map<String, Object> params = MapUtil.newHashMap();
		params.put("q", chineseText);
		params.put("from", "zh");
		params.put("to", convertLanguage(toLanguage));
		params.put("appid", appId);
		params.put("salt", salt);
		params.put("sign", SecureUtil.md5(CharSequenceUtil.concat(true, appId, chineseText, salt, secret)));
		return params;
	}

}
