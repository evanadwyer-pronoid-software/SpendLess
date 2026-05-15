# SpendLess — Agent Operating Manual

This file is the single source of truth for any agent (Claude Code, Codex, Copilot CLI, Gemini CLI, subagents) working in this repo. Read it top to bottom before making any change. When a constraint here conflicts with a skill, **this file wins**.

---

## 1. Goal

Build **SpendLess**, a personal Android app for tracking income and expenses with a strong security and encryption posture. The user owns their financial data and the app must protect it as such: nothing sensitive lives in plaintext on disk, in logs, or in process memory longer than necessary.

## 2. Context

- **Project:** `com.plcoding.spendlesssample` (will be renamed when the multi-module split lands).
- **Stack:** Kotlin 2.2.10, AGP 9.1.1, Gradle 9.3.1, Jetpack Compose (BOM 2024.09.00), Material3, version catalog (`gradle/libs.versions.toml`), JDK 11.
- **Min/Target/Compile SDK:** 26 / 36 / 36.
- **Distribution:** Offline-first. There is **no remote backend** until one is explicitly added — agents must not invent network calls, sync workers, or auth flows that assume a server.
- **Architecture (mandatory from day one):** Feature-layered Clean Architecture — `:app` assembles `:core:*` and `:feature:<name>:presentation|domain|data`. Domain depends on nothing. Features never depend on each other; cross-feature shared code lives in `:core:domain` or `:core:presentation`. See the `android-module-structure` skill for the canonical layout.
- **DI (mandatory from day one):** Koin, one module per layer per feature, all assembled in `:app`. See `android-di-koin`.
- **Persistence:** Room, **encrypted with SQLCipher**. The DB passphrase is generated once and wrapped by an AndroidKeyStore-backed AES key. Plain `Room.databaseBuilder` is forbidden in production code paths.
- **Reactive layer:** Kotlin Flows (StateFlow for UI state, Room/DataStore Flows for sources, Channel for one-shot UI events). See `kotlin-flows`.
- **Errors:** Project-wide typed `Result<T, E>` + `DataError` per `android-error-handling`. No `try`/`catch` leaking out of the data layer; no untyped `Throwable` in domain or presentation signatures.

## 3. Constraints — when to invoke which skill

Skills are stored in `~/.claude/skills/<name>/SKILL.md` and loaded on demand via the `Skill` tool (Claude Code) or the equivalent in your runtime. **Never use the `Read` tool on a skill file** — invoke the skill so its instructions are properly loaded. Match the situation below to its skill and load the skill **before** writing code.

### Project structure & build

- **`android-module-structure`** — Invoke when setting up the project, creating any new feature module, splitting code into `:core:*`, or asking "where does this go?". Defines the canonical `:app + :core:* + :feature:*:presentation|domain|data` layout that every other skill assumes.
- **`android-version-catalog`** — Invoke for any dependency/plugin/bundle change, version bump, or non-trivial Gradle config. `gradle/libs.versions.toml` and `:build-logic` convention plugins are the *only* places versions and Gradle setup live; never inline a version string in a `build.gradle.kts`.
- **`android-cli`** — Invoke for SDK install/upgrade, emulator/device orchestration, project scaffolding from templates, or environment diagnostics via the `android` CLI tool.

### Dependency injection

- **`android-di-koin`** — Invoke whenever you define a Koin module, provide a ViewModel/repository/data source, inject a dependency, or wire feature modules in `:app`. Koin is mandatory; constructor-reference overloads (`singleOf`, `viewModelOf`, `factoryOf`) are preferred.

### Presentation layer (Compose)

- **`android-presentation-mvi`** — Invoke when creating or modifying a ViewModel, screen State/Action/Event, Root/Screen composable split, `UiText` error mapping, or `SavedStateHandle`-backed process-death handling. This is the only sanctioned shape of a screen.
- **`android-compose-architecture`** — Invoke for *screen-level* Compose decisions: recomposition, `@Stable`, state ownership, `LaunchedEffect`/`DisposableEffect`, lazy-layout performance, `collectAsStateWithLifecycle`, deferred state reads, and `TextField` state dispatching.
- **`android-compose-components`** — Invoke when writing any composable that is **not** a Root/Screen — buttons, cards, list items, dialogs, fields, slots, modifier extensions, previews per state, IconButton wrapping, design-system reuse.
- **`compose-adaptive-layouts`** — Invoke **only** when a screen's composable tree must structurally differ across configurations (Column→Row, elements appear/disappear, full-bleed↔centered card). Do **not** invoke for plain responsive sizing achievable with `fillMaxWidth`/`weight`/`widthIn`.
- **`android-navigation`** — Invoke when defining routes, adding a screen to a nav graph, navigating between features, or wiring graphs in `:app`. Routes are `@Serializable` objects; cross-feature navigation goes through callbacks, never direct dependencies.

### Domain & data layer

- **`android-data-layer`** — Invoke when creating any data source, repository, DTO, Room entity, mapper, Ktor client, or anything that crosses an I/O boundary. Distinguishes data source (single source) from repository (coordinates multiple sources).
- **`android-error-handling`** — Invoke any time typed errors, validation results, or success/failure flows are defined — including in domain, presentation, and validators. The shared `Result<T, E>` and `DataError` types live in `:core:domain`.
- **`android-background`** — Invoke when scheduling background work (WorkManager one-time/periodic/expedited), building a foreground service, observing work progress, or wiring `WorkerFactory` with Koin.

### Concurrency

- **`kotlin-coroutines`** — Invoke when writing or reviewing `suspend` functions, coroutine scopes, parallel execution, dispatcher choice, blocking-code wrapping, or anything cancellation-related (`ensureActive`, `NonCancellable`, `Mutex`, `limitedParallelism`).
- **`kotlin-flows`** — Invoke for any `Flow`/`StateFlow`/`SharedFlow`/`Channel`/`callbackFlow` work, `stateIn`/`shareIn`, operators, `flowOn`, backpressure, lifecycle-aware collection, or offline-first reactive chains.

### Testing

- **`android-unit-testing`** — Invoke for tests of any pure-logic unit (ViewModel, validator, mapper, use case, formatter, reducer). Mandatory for every new such class. JUnit5 + AssertK + Turbine + fakes-over-mocks + `UnconfinedTestDispatcher`.
- **`android-integration-testing`** — Invoke for tests that cross at least one I/O boundary: Room (in-memory), Ktor `MockEngine`, file system, fake `Clock`, `WorkManagerTestInitHelper`. Instrumented, no Robolectric.
- **`android-ui-testing`** — Invoke for instrumented Compose tests via `createAndroidComposeRule<ComponentActivity>()` — UI+real ViewModel integration, feature E2E, or Robot pattern. Mind the Compose 1.11.0 dispatcher change (`StandardTestDispatcher` requires explicit `advanceUntilIdle`/`mainClock` advancement).
- **`kotlin-mutation-testing`** — Invoke after writing or reviewing any Kotlin test class to confirm the tests actually catch regressions; surfaces fake, over-mocked, or assertionless tests.

### UI verification (after every screen)

- **`android-ui-verify`** — Invoke after finishing a screen to **structurally** compare layout hierarchy, text content, and dp-converted sizing (±4dp tolerance) against the Figma reference, using the device's UI Automator tree.
- **`android-ui-screenshot-verify`** — Invoke after finishing a screen to **visually** compare a single screen on a single viewport against Figma using the `android` CLI's annotated screenshot tools — catches color drift, overlap, clipping, truncation, wrong icon shape, contrast issues that the structural skill cannot see.
- **`figma-to-android-vector`** — Invoke **every** time you create an Android `VectorDrawable` XML or Compose `ImageVector` from a Figma icon. Path data must come from the actual SVG `d` attribute — never invent, simplify, or recreate paths from a screenshot.

### Committing

- **`git-commit`** — Invoke after every logical unit of work to split pending changes into one or more atomic Conventional Commits. Stops at local commits — does not branch or push. Do **not** invoke if the build gate or any task tests are red (see §4).

## 4. Acceptance criteria — what "done" means

A change is **not done** until **all** of the following are true. Skipping any of them is not a shortcut — it's a defect.

1. **`./gradlew build` passes** for the entire project, all modules. This compiles every variant and runs all JVM unit tests + lint. A red build means the task is unfinished, regardless of what the diff looks like.
2. **All existing tests still pass.** No `@Ignore`, no commented-out assertions, no `--no-verify`, no skipping hooks.
3. **New tests exist** for every new pure-logic unit (ViewModel, validator, mapper, use case, formatter, reducer) per `android-unit-testing`. New I/O boundaries get integration tests per `android-integration-testing`. New screens get a UI/E2E test per `android-ui-testing` when the screen has non-trivial behavior.
4. **UI matches Figma.** Every new or modified screen has been verified against the Figma reference using `android-ui-verify` (structural) and/or `android-ui-screenshot-verify` (visual). No improvising on visuals — Figma is the only source of truth.
5. **Security invariants hold:**
   - The Room database is opened with SQLCipher; the passphrase is wrapped by an AndroidKeyStore key and never written to plaintext storage, logs, analytics, or crash reports.
   - No `Log.*` call prints transaction amounts, notes, balances, PINs, session tokens, or any field that could re-identify a user. When unsure, do not log.
   - No sensitive data leaves the device (offline-first; no network until the user explicitly opts into a backend).
6. **Skills were actually used.** If `android-presentation-mvi` applies and you didn't load it, that's a defect — re-do the work.
7. **The work was committed via `git-commit`.** Each logical unit is a separate Conventional Commit. **Do not commit if any of #1–#6 are red.** If a commit's pre-commit hook fails, fix the issue and create a *new* commit — do **not** `--amend`, do **not** `--no-verify`.

## 5. Verification method — how success is measured

Run, in order, before considering any task done:

1. `./gradlew build` from the repo root. **Must pass cleanly.** This is the gate.
2. If the task touched UI: install on a connected device or emulator (`./gradlew installDebug`), launch the affected screen, and run `android-ui-verify` followed by `android-ui-screenshot-verify` against the relevant Figma node. Resolve any reported deltas before proceeding.
3. If the task added or changed pure-logic code: confirm new tests exist (`android-unit-testing`) and run `kotlin-mutation-testing` on the new test class to confirm it has teeth.
4. If the task added or changed I/O code: confirm an integration test exists (`android-integration-testing`) and runs as part of `./gradlew build`.
5. If `./gradlew build` is green, all relevant tests pass, and UI verification (when applicable) passed, invoke `git-commit` to land the work as one or more atomic Conventional Commits.

If you cannot run the build (e.g., environment missing the Android SDK), say so explicitly and stop — do not claim success on faith.

## 6. Tools & environment

### Figma MCP — `plugin:figma:figma`

**Figma is the single source of truth for every visual decision.** Never improvise colors, spacing, sizes, typography, iconography, or component shapes from memory.

- Project Figma file: `https://www.figma.com/design/ftCIcpiLxrMhckuzDmYQWL/SpendLess`
- Anchor entry point: `?node-id=401-935` (`fileKey=ftCIcpiLxrMhckuzDmYQWL`, `nodeId=401:935`).
- For any UI work, **first** call `mcp__plugin_figma_figma__get_design_context` (and `get_screenshot` / `get_metadata` / `get_variable_defs` as needed) on the relevant node to pull the live design context. Treat the returned React+Tailwind output as a *reference*; translate to Compose using the existing Material3 theme and the project's design tokens.
- For icons, follow `figma-to-android-vector` — pull the actual SVG and copy `d` verbatim into `pathData`.
- Do **not** upload code, screenshots, or anything from this repo back into Figma unless the user explicitly asks.

### Android tooling

- Build: `./gradlew build` (canonical gate). Faster local loops: `./gradlew assembleDebug`, `./gradlew testDebugUnitTest`. Final word is always `build`.
- Device/emulator orchestration, SDK install, environment diagnostics: use the `android` CLI per the `android-cli` skill.
- UI verification on a running device: `android-ui-verify` (structural) and `android-ui-screenshot-verify` (visual) — both require a live device or emulator.

### Git

- Commits go through the `git-commit` skill: atomic, Conventional Commits, no Claude co-author footer, no AI attribution. Local-only; no `push`, no force flags, no `--amend`, no `--no-verify`. If a hook fails, fix and create a new commit.
- Don't run destructive git commands (`reset --hard`, `clean -f`, `branch -D`) without explicit user approval.

---

**If anything above is unclear, ambiguous, or contradicts a user instruction, ask before proceeding.** A two-sentence clarifying question costs less than a wrong implementation.
