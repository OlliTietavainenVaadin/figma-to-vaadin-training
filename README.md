# Figma to Vaadin Training

A hands-on training project for practising **Figma-to-Vaadin translation with Claude Code**. You'll use
Figma designs as a source of truth and have Claude Code generate Vaadin Flow code from them, following
a disciplined workflow (inspect design → check annotations → consult Vaadin docs → implement with proper
components and Lumo theming).

Exercise descriptions are provided separately. This repository gives you the environment to do them in.

## What's in the Box

- A Vaadin 25 / Spring Boot 4 / Java 21 starter application
- Two project-local Claude skills under `.claude/skills/` that encode the workflow:
  - **`figma-to-vaadin`** — translates Figma designs into Vaadin Flow code. Extracts design context,
    checks component annotations, reviews Vaadin docs, and implements using proper components, themes,
    and Lumo utility classes instead of raw styles.
  - **`figma-to-lumo-theme`** — maps Figma design tokens (colors, typography, sizing, shapes) into
    Lumo CSS custom properties in `src/main/resources/META-INF/resources/styles.css`.
- Several feature packages under `src/main/java/com/example/` (`customers`, `employees`,
  `hotels`, `products`, `reports`, `staffallocation`, `employeeprofile`) left in place as
  **reference outputs** from previous runs of the exercises. Look at them for inspiration, or
  ignore them.

## Prerequisites

- **JDK 21**
- **Claude Code** with access to the **Figma MCP** and **Vaadin MCP** servers — both are required
  for the skills to work. The skills call tools like `get_design_context`, `get_variable_defs`,
  `search_vaadin_docs`, and `get_full_document`.
- A Figma file for the exercise you're working on (supplied separately)

## Running the Application

Start in development mode:

```bash
./mvnw
```

The app runs at <http://localhost:8080> and opens a browser automatically.

Run tests:

```bash
./mvnw test
```

Build a production JAR:

```bash
./mvnw -Pproduction package
```

## Project Structure

```
src/main/java/com/example/
├── base/ui/              # Shared UI (MainLayout, ViewToolbar)
├── examplefeature/       # Reference feature — template for new features
├── customers/            # Reference output from a previous exercise
├── employees/            #   "
├── hotels/               #   "
├── products/             #   "
├── reports/              #   "
├── staffallocation/      #   "
├── employeeprofile/      #   "
└── Application.java      # Spring Boot entry point
```

Each feature package follows the same template: entity, repository, service, and a `ui/` package for
views. See `CLAUDE.md` for the detailed feature template and testing patterns.

Theme customisation lives in `src/main/resources/META-INF/resources/styles.css`. It's referenced via
`@StyleSheet("styles.css")` on the `AppShellConfigurator` (the Vaadin 25 approach — no `frontend/themes`
folder).

## How the Skills Get Invoked

Claude Code picks up the skills automatically based on their descriptions. Typical triggers:

- Paste a Figma node URL and ask Claude to implement it → `figma-to-vaadin` runs
- Ask Claude to apply a Figma file's design tokens to the app's theme → `figma-to-lumo-theme` runs

You can also invoke them explicitly with `/figma-to-vaadin` or `/figma-to-lumo-theme`.

## Further Reading

- `CLAUDE.md` — project conventions (package layout, entity/service/view patterns, testing)
- [Vaadin 25 Building Apps guides](https://vaadin.com/docs/v25/building-apps)
- The two `SKILL.md` files under `.claude/skills/` — the full workflow the skills follow