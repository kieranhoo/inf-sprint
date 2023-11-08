import { BrowserRouter } from "react-router-dom";
import RouterList from './routers';
import "./App.css"
import { Provider } from "react-redux";
import { store, persistor } from "./redux/store/store"
import { PersistGate } from 'redux-persist/integration/react';
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from 'react-toastify';

function App() {
  return (
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}>
        <BrowserRouter>
          <ToastContainer position="top-right"
            autoClose={3000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            theme="light" />
          <RouterList></RouterList>
        </BrowserRouter>
      </PersistGate>
    </Provider>
  )
}

export default App;
