const {readFileSync} = require('fs')
const {join} = require('path')

module.exports = {
  branches: [
    "main"
  ],
  plugins: [
    [
      "@semantic-release/commit-analyzer",
      {
        releaseRules: [
          {
            type: "feat",
            release: "minor"
          },
          {
            type: "perf",
            release: "patch"
          },
          {
            type: "fix",
            release: "patch"
          },
          {
            type: "refactor",
            release: "patch"
          },
          {
            type: "deps",
            release: "patch"
          },
          {
            type: "build",
            release: "patch"
          },
          {
            type: "chore",
            release: "patch"
          },
          {
            type: "docs",
            scope: "README",
            release: "patch"
          },
          {
            type: "style",
            release: "patch"
          }
        ],
        parserOpts: {
          noteKeywords: [
            "BREAKING CHANGE",
            "BREAKING CHANGES"
          ]
        }
      }
    ],
    [
      "@semantic-release/release-notes-generator",
      {
        preset: "conventionalcommits",
        parserOpts: {
          noteKeywords: [
            "BREAKING CHANGE",
            "BREAKING CHANGES",
            "BREAKING"
          ]
        },
        writerOpts: {
          headerPartial: readFileSync(join(__dirname, '.github/templates/header.hbs'), 'utf-8'),
          commitPartial: readFileSync(join(__dirname, '.github/templates/commit.hbs'), 'utf-8'),
          footerPartial: readFileSync(join(__dirname, '.github/templates/footer.hbs'), 'utf-8'),
          mainTemplate: readFileSync(join(__dirname, '.github/templates/template.hbs'), 'utf-8')
        },
        presetConfig: {
          types: [
            {
              type: "feat",
              section: "🧰 Features",
              hidden: false
            },
            {
              type: "perf",
              section: "⚙ Performance Improvements",
              hidden: false
            },
            {
              type: "fix",
              section: "🐛 Bugs fixed",
              hidden: false
            },
            {
              type: "refactor",
              section: "✏ Refactor",
              hidden: false
            },
            {
              type: "deps",
              section: "📥 Dependency Updates",
              hidden: false
            },
            {
              type: "chore",
              section: "📡 Conventional Changes",
              hidden: false
            },
            {
              type: "build",
              section: "⚒ Build Updates",
              hidden: false
            },
            {
              type: "docs",
              section: "📖 Documentation",
              hidden: false
            },
            {
              type: "style",
              section: "📜 Styling updates",
              hidden: false
            }
          ]
        }
      }
    ],
    [
      "@semantic-release/github",
      {
        assets: [ { path: "**/build/libs/**-shaded.jar" } ],
        labels: false,
        failTitle: false,
        failComment: false,
        successComment: false,
        releasedLabels: false,
        addReleases: false
      }
    ]
  ]
}
