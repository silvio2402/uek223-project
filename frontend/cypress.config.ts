import { defineConfig } from "cypress"

// eslint-disable-next-line @typescript-eslint/no-require-imports
const webpackPreprocessor = require("@cypress/webpack-batteries-included-preprocessor")

export default defineConfig({
  e2e: {
    setupNodeEvents(on) {
      // implement node event listeners here
      on(
        "file:preprocessor",
        webpackPreprocessor({
          typescript: require.resolve("typescript"),
        })
      )
    },
  },
})
