import { createTheme } from "@mui/material/styles";

export const darkTheme = createTheme({
  palette: {
    mode: "dark",

    // Primary color used for buttons, highlights, etc.
    primary: {
      main: "#e91e63",
    },

    // Secondary color, optional usage like accents or chips
    secondary: {
      main: "#5A20CB",
    },
    black: {
      main: "#242B2E",
    },

    // Background colors for the overall app and paper components (like cards)
    background: {
      main: "#000000",
      default: "#0D0D0D", // Default background for the app
      paper: "#0D0D0D", // Background for surfaces like cards, dialogs, etc.
    },
    textColor: {
      main: "#111111",
    },
  },
});
