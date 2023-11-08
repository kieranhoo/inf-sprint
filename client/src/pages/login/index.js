import React, { useEffect } from 'react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { useDispatch, useSelector } from 'react-redux';
import { loginRequest } from '../../redux/reducers/authSlice';
import { selectError, selectIsAuthenticated } from '../../redux/selectors';
import { toast } from "react-toastify"
import { useNavigate } from "react-router-dom"

const Login = () => {
    const dispatch = useDispatch();
    const isAuthenticated = useSelector(selectIsAuthenticated)
    const isLoading = useSelector((state) => state.auth.isLoading);
    const error = useSelector(selectError)
    const navigate = useNavigate()
    const formik = useFormik({
        initialValues: {
            email: '',
            password: '',
        },
        validationSchema: Yup.object({
            email: Yup.string().required('Email is required'),
            password: Yup.string().required('Password is required'),
        }),
        onSubmit: (values) => {
            dispatch(loginRequest(values))
        },
    });
    useEffect(() => {
        if (isAuthenticated) {
            toast.success("Đăng nhập thành công")
            navigate('/dashboard')
        }
        if (error) {
            toast.error(error)
        }
    }, [isAuthenticated, error]) // eslint-disable-line react-hooks/exhaustive-deps
    return (
        <div>
            <form onSubmit={formik.handleSubmit} className="max-w-sm mx-auto p-4 bg-white rounded shadow mt-10">
                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="email">
                        Email
                    </label>
                    <input
                        type="text"
                        id="email"
                        name="email"
                        {...formik.getFieldProps('email')}
                        className={`border rounded w-full py-2 px-3 ${formik.errors.email && formik.touched.email ? 'border-red-500' : ''}`}
                        placeholder="Enter your email"
                    />
                    {formik.errors.email && formik.touched.email && <div className="text-red-500 text-sm mt-1">{formik.errors.email}</div>}
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="password">
                        Password
                    </label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        {...formik.getFieldProps('password')}
                        className={`border rounded w-full py-2 px-3 ${formik.errors.password && formik.touched.password ? 'border-red-500' : ''}`}
                        placeholder="Enter your password"
                    />
                    {formik.errors.password && formik.touched.password && <div className="text-red-500 text-sm mt-1">{formik.errors.password}</div>}
                </div>
                <button
                    type="submit"
                    className={`bg-blue-500 text-white px-4 py-2 rounded ${isLoading ? 'opacity-50 cursor-not-allowed' : 'hover:bg-blue-600'}`}
                    disabled={isLoading}
                >

                    {isLoading ? 'Logging in...' : 'Login'}
                </button>
            </form>
        </div>
    );
};

export default Login;
