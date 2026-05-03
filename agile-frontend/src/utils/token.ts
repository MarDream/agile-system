const TOKEN_STORAGE_KEY =
  (import.meta as any).env?.VUE_APP_tokenTableName || 'ab-token'

export const getToken = (): string => {
  return localStorage.getItem(TOKEN_STORAGE_KEY) || ''
}

export const setToken = (token: string): void => {
  localStorage.setItem(TOKEN_STORAGE_KEY, token)
}

export const removeToken = (): void => {
  localStorage.removeItem(TOKEN_STORAGE_KEY)
}
