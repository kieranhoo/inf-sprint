import { configureStore } from '@reduxjs/toolkit';
import createSagaMiddleware from 'redux-saga';
import rootReducer from '../reducers';
import rootSaga from '../sagas';
import storage from 'redux-persist/lib/storage';
import { persistStore, persistReducer } from "redux-persist";

const sagaMiddleware = createSagaMiddleware();

const persistConfig = {
    key: 'root',
    storage, // sử dụng redux-persist/lib/storage để lưu vào localStorage
};
const persistedReducer = persistReducer(persistConfig, rootReducer);
const store = configureStore({
    reducer: persistedReducer,
    middleware: [sagaMiddleware],
});
const persistor = persistStore(store);

sagaMiddleware.run(rootSaga);

export { store, persistor};


