# PRISM install record

PRISM 0.6.3, installed 2026-10-31 by Claude Code into `SpendLess` (module `:app`).

## 1. What landed

**Theirs (edit freely):**
- `tooling/prism-rules`, `tooling/konsist`: the 72 detekt rules and the architecture tests
- `build-logic/`: PRISM's four convention plugins (`prism.ktlint`, `prism.detekt`, `prism.jacoco`, `prism.static-analysis`)
- `detekt.yml`, `.editorconfig`, `.gitignore` (appended), `.gitattributes` (new)
- `gradle/libs.versions.toml` (detekt, ktlint, konsist, assertk, junit5, coroutines, jacoco, composeRules and `kotlin-jvm` added; their `agp` and `kotlin = 2.4.21` kept)
- `settings.gradle.kts`, root `build.gradle.kts`, `app/build.gradle.kts` (wiring only)
- `./prism`, `./prism.cmd`
- `.claude/settings.json`, `.claude/agents/*`, `.claude/skills/{verify,finalize,prism-burndown}`
- `.git/hooks/pre-push`
- 7 files under `app/src` reformatted by `ktlintFormat`

**PRISM's (leave alone):** `.prism/` (engine, assets, templates, adapters, docs, `prism.json`, this record).

**ADOPTED (replaced existing):** `.claude/agents/mutation-worker.md` is now PRISM's copy; the previous project-local version was removed.

## 2. Parameters

| Parameter | Value | How |
|---|---|---|
| PACKAGE_ROOT | `com.plcoding.spendlesssample` | ANSWERED (derived from `package` declarations) |
| MODULE_PACKAGE_ROOTS | `"app" to "com.plcoding.spendlesssample"` | ANSWERED (derived from settings.gradle.kts) |
| LAYER_PURITY_DOMAIN_PACKAGE | `com.plcoding.spendlesssample.core.domain` | ANSWERED (derived; no `.domain` package exists, so the rule SKIPS) |
| PALETTE_OBJECT / EXTENDED_TOKEN_HOLDER | `none` / `none` | ANSWERED (probe) |
| THEME_OBJECT | `SpendLessSampleTheme` | ANSWERED (probe) |
| PALETTE_RULES_ACTIVE / THEME_RULES_ACTIVE | `false` / `true` | ANSWERED (probe) |
| BASE_BRANCH | `origin/initial` | CHOSEN by the human: all work goes straight to `initial`, no PRs, so the diff is local vs the remote `initial`. Not the local branch name: `initial` would give an empty diff of committed work. |
| POSTURE | `enforce` | CHOSEN (forced by the human) |
| HARNESS | `claude-code` | set after render (the render defaulted it to `none`) |
| pre-push floor | installed | CHOSEN |
| thresholds `overrides` example | removed | CHOSEN. Default stays 80%, no per-module overrides |

## 3. Probe output (verbatim)

```
PRISM design-system probe

  PALETTE_OBJECT         none                     no declaration holds three or more raw Color literals
  EXTENDED_TOKEN_HOLDER  none                     no class declares two or more Color-typed properties
  THEME_OBJECT           SpendLessSampleTheme     app/src/main/java/com/plcoding/spendlesssample/ui/theme/Theme.kt:8
  PALETTE_RULES_ACTIVE   false
  THEME_RULES_ACTIVE     true

  34 raw Color literals sit at top level, in files including:
    app/src/main/java/com/plcoding/spendlesssample/ui/theme/Color.kt:5
    ... (Color.kt:6-9)
  There is no declaration to name. PALETTE_RULES_ACTIVE=false is
  the correct answer, and those colours are checked by no rule.
```

Re-run with `./prism probe`.

## 4. Checked by no rule

- **Raw colours:** the 34 top-level `Color(0x…)` vals in `ui/theme/Color.kt` and their use. `ThemeColorDirectUse` and `UnwiredThemeColor` are off, and `RawColorLiteral` catches hex literals, not named vals.
- **Layer purity:** no `.domain` package exists, so the Ktor/serialization purity rule SKIPS.
- **Konsist DTO/repository rules:** skipped, because the project has no DTOs or repositories yet. They apply as soon as such classes appear.
- **Coverage:** no measurement exists. Gate 3 (push) denies pushes for `:app` until a coverage result is recorded (`./prism coverage :app`, needs an emulator).

## Install-time measurements

- Active rules: 70 of 72 (expected: 72 − 2).
- First `staticAnalysis` after `ktlintFormat`: **detekt 8**, **ktlint 4**, **konsist 0 failures**. Posture is `enforce` with no baseline, so the build is **red** and pushes are refused until fixed.
  - detekt: NonAssertKAssertion 2, JUnit4InJvmUnitTest 2, CompositionLocalAllowlist 2, PreviewMustBePrivate 1, ClassBodyMissingLeadingBlankLine 1
  - ktlint: no-wildcard-imports 4 (not auto-correctable)
- Canary: a planted `println` was reported as `NoConsoleLogging` and failed the build; the file was restored.

## Change after install (2026-11-03)

Coverage was switched to `observe` (`.prism/scope.json`, posture `mixed`) so the first push is not denied for lack of a coverage result. `:app` has only the template tests. detekt, ktlint and konsist still enforce. Re-enable: record a measurement with `./prism coverage :app`, then `./prism promote --engine coverage`.
