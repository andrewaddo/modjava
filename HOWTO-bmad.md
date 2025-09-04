# How to use BMAD with Gemini

## Gemini web / gem

1. TODO: this is cool, but I would rather stay on the CLI

## Gemini CLI

1. Installation

```bash
npx bmad-method install
```

1. Configure Gemini CLI to ginore .gitignore so it can reach .bmad-core/agents. On .gemini/settings.json
   
```bash
   "fileFiltering": {
      "respectGitIgnore": false
   }
```

1. To start

```prompt
@.bmad-core/agents/bmad-master.md who are you? what do you do? what are other roles and what do they do ?
```

1. Sample
   
```prompt
@.bmad-core/agents/pm.md @.bmad-core/tasks/brownfield-create-epic.md
@.bmad-core/agents/sm.md Please draft the stories for the AI Product Information epic.
@.bmad-core/agents/dev.md *develop-story 1.1

-- OR using the generated .gemini/bmad-method/GEMINI.md
*qa
*dev
```