import logo from './logo.svg';
import './App.css';
import { Navbar } from './component/Navbar/Navbar';
import { CssBaseline, ThemeProvider } from '@mui/material';
import { darkTheme } from './Theme/DarkTheme';

function App() {
  return (
    
    // Wrapping the entire app inside ThemeProvider to apply the custom theme
    <ThemeProvider theme={darkTheme}>

      {/* CssBaseline resets default browser styles to make 
      the app look consistent everywhere */}
      <CssBaseline/>

      <Navbar/>

    </ThemeProvider>
  );
}

export default App;
