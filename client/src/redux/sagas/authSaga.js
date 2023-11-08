import { takeLatest, put, call } from 'redux-saga/effects';
import { loginSuccess, loginFailure, logoutSuccess, logoutRequest, logoutFailure } from "../reducers/authSlice";
import { apiLogin, apiRefreshToken, apiLogout } from '../../services';

function* loginUser(action) {
  try {
    const response = yield call(apiLogin, action.payload);
    yield put(loginSuccess(response));
  } catch (error) {
    yield put(loginFailure(error));
  }
}

function* refreshToken(action) {
  try {
    const response = yield call(apiRefreshToken, action.payload);
    yield put(loginSuccess(response));
  } catch (error) {
    yield put(logoutRequest()); // Dispatch action logoutRequest() khi refreshToken hết hạn hoặc không hợp lệ
  }
}

function* logoutUser(action) {
  try {
    yield call(apiLogout, action.payload);
    yield put(logoutSuccess()); // Dispatch action logoutSuccess() khi logout thành công
  } catch (error) {
    yield put(logoutFailure(error));
  }
}

function* authSaga() {
  yield takeLatest('auth/loginRequest', loginUser);
  yield takeLatest('auth/refreshToken', refreshToken);
  yield takeLatest('auth/logoutRequest', logoutUser); // Thêm takeLatest cho action logoutRequest
}

export default authSaga;
