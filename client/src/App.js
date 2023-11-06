import { BrowserRouter } from "react-router-dom";
import RouterList from './routers';
import "./App.css"

function App() {
  return (
    <BrowserRouter>
      <RouterList></RouterList>
    </BrowserRouter>
  )
}

export default App;
