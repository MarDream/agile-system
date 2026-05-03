# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository shape

This repository now contains both parts of the workspace:

- Backend: the Java/Spring multi-module project rooted at `pom.xml` under `agile-backend/`
- Frontend: the Vue 3 + Vite project in `agile-frontend/`

Most feature work spans both sides: backend REST/resource/auth changes in the Maven modules, and frontend route/view/menu/API wiring in `agile-frontend`.

## Common commands

### Backend

Build all backend modules:

```bash
mvn clean install -DskipTests
```

Run the main backend app:

```bash
mvn -pl agile-backend/ab-spring-boot/ab-spring-boot-app spring-boot:run
```

Package only the runnable backend app:

```bash
mvn -pl agile-backend/ab-spring-boot/ab-spring-boot-app -am package -DskipTests
```

Run all backend tests:

```bash
mvn test
```

Run a single backend module's tests:

```bash
mvn -pl agile-backend/ab-base/ab-base-common test
```

Run a single backend test class:

```bash
mvn -pl agile-backend/ab-base/ab-base-common -Dtest=MemoryCacheTest test
```

Run the backend code generator:

```bash
mvn -pl agile-backend/ab-code-generator spring-boot:run
```

### Frontend (`agile-frontend`)

Install dependencies:

```bash
npm install
```

Start the frontend dev server:

```bash
npm run dev
```

Build the frontend:

```bash
npm run build
```

Preview the frontend build:

```bash
npm run preview
```

## Runtime and local setup

### Backend runtime

- Main runtime config: `agile-backend/ab-spring-boot/ab-spring-boot-app/src/main/resources/application.yml`
- Default profile: `dev`
- Expected local services: MySQL on `localhost:3306`, Redis on `localhost:6379`
- Initialize the database with a script from `docs/sql/mysql/full/`
- `docs/sql/mysql/README.md` explicitly says to use the `full` SQL directory
- Auth ignore paths, CSRF ignore hosts, token storage mode, cache regions, scheduler toggle, upload backend, and MQ mode are all configured in `application.yml`
- `agile-backend/ab-spring-boot/ab-spring-boot-app` depends on `src/main/resources/lib/ab-dependencies-2.8.0.jar` via system scope, and packaging relies on `includeSystemScope=true`

### Frontend runtime

- Frontend app root: `agile-frontend/`
- Dev server config: `agile-frontend/vite.config.ts`
- Default frontend port: `8088`
- Dev proxy: `/api` → `http://localhost:8080/`
- Frontend request/env settings: `agile-frontend/.env`
- App-level defaults such as app name, dashboard route, token header name, token prefix, layout, and local-storage encryption are in `agile-frontend/src/config/defaultConfig.ts`
- Frontend bootstraps the local component packages from `agile-frontend/agilebpm/` via file dependencies in `agile-frontend/package.json`

## Backend architecture

The root `pom.xml` is an aggregator. Main backend modules:

- `ab-base`: shared controller/manager abstractions such as `AbBaseController`, `AbCrudController`, `AbBaseManager`, and `AbBaseManagerImpl`
- `ab-component`: cross-cutting infrastructure such as cache, Redis, messaging, upload, pub/sub, and Groovy scripting
- `ab-org`, `ab-sys`, `ab-auth`, `ab-cms`: business/platform domains built on top of `ab-base`
- `ab-spring-boot`: application assembly and runtime entrypoint
- `ab-code-generator`: standalone Spring Boot code generator

`ab-demo` exists as sample CRUD-style code but is not part of the root Maven reactor.

Most backend modules follow the same structure:

1. `rest/controller` exposes endpoints
2. `core/manager` defines business interfaces
3. `core/manager/impl` implements logic, usually by extending `AbBaseManagerImpl<T>`
4. `core/mapper` plus `src/main/resources/**/mapper/*.xml` implement persistence

Important backend implications:

- Many CRUD endpoints inherit shared behavior from `AbCrudController`, so edits in `ab-base` can affect multiple modules
- The codebase uses MyBatis-Plus, but real query behavior often lives in XML mappers; always inspect both the Java mapper interface and matching XML
- Authentication spans `ab-auth-api`, `ab-auth-core`, `ab-auth-spring-security-oauth2`, and runtime settings in `application.yml`
- `agile-backend/ab-spring-boot/ab-spring-boot-app` is the final assembly point for what is actually enabled at runtime

## Frontend architecture

The frontend is a Vue 3 + Vite + Pinia + Element Plus SPA under `agile-frontend/`.

Key structure:

- `src/main.ts`: app bootstrap; registers Element Plus, Pinia, router, i18n, and the local component/design packages
- `src/router/index.ts`: global router setup, static route registration, login redirect logic, token-expiry checks, and special designer/preview routes
- `src/router/modules/*.ts`: module-scoped route trees for `biz`, `bpm`, `cms`, `org`, and `sys`
- `src/store/modules/*.ts`: Pinia stores for menus, user state, global UI state, tabs, iframe state, and store adapters
- `src/utils/request.js`: shared Axios wrapper and auth header injection
- `src/views/**`: feature pages, grouped by backend domain names
- `src/config/defaultConfig.ts`: frontend defaults used by routing, auth header naming, layout, dashboard, and branding

Important frontend implications:

- The frontend is organized by the same domain split as the backend (`biz`, `bpm`, `cms`, `org`, `sys`), so cross-layer changes are usually easy to trace by domain
- Route metadata drives menu behavior, tabs, affix behavior, and dynamic tab handling
- Menu state is persisted in Pinia/local storage through `routerMenusStore`
- Auth headers are injected centrally by `src/utils/request.js`, using the token header and prefix from config
- The designer features are not ordinary local Vue components; they are bootstrapped from the packaged local libraries under `agile-frontend/agilebpm/`

## Fast orientation for edits

### If you are changing backend CRUD behavior

Check in this order:

1. The concrete controller in the target module
2. Whether it inherits core behavior from `AbCrudController`
3. The target manager interface and manager impl
4. The mapper interface and matching XML mapper

### If you are changing backend queries or persistence

Check both:

- Java mapper interfaces under `core/mapper`
- XML mapper files under `src/main/resources/**/mapper/*.xml`

Do not assume the MyBatis-Plus interface tells the whole story.

### If you are changing login, token, permission, or session behavior

Inspect together:

- `ab-auth-core`
- `ab-auth-spring-security-oauth2`
- `agile-backend/ab-spring-boot/ab-spring-boot-app/src/main/resources/application.yml`
- `agile-frontend/src/utils/request.js`
- `agile-frontend/src/config/defaultConfig.ts`
- `agile-frontend/src/router/index.ts`
- `agile-frontend/src/views/login/**`

Backend auth changes are usually incomplete unless the frontend token/header/login flow still matches.

### If you are changing menus, routes, or page entry points

Check together:

- `agile-frontend/src/router/index.ts`
- `agile-frontend/src/router/modules/*.ts`
- The matching page under `agile-frontend/src/views/**`
- Backend system resource / authorization APIs if the menu is driven by server-side resource definitions

The frontend README also notes that adding a new page usually means three pieces: create the view, add the route, and configure the menu resource.

### If you are changing request URLs or frontend-backend wiring

Check together:

- `agile-frontend/vite.config.ts` for `/api` proxy behavior
- `agile-frontend/.env` for request conventions
- `agile-frontend/src/utils/request.js` for interceptor behavior
- The backend controller path and auth-ignore rules in `application.yml`

### If you are changing designer-related features

Check together:

- `agile-frontend/src/main.ts`
- `agile-frontend/package.json`
- `agile-frontend/agilebpm/`
- The corresponding designer view such as form or BPM designer pages

These features depend on local packaged libraries, not only normal source files under `src/views`.
