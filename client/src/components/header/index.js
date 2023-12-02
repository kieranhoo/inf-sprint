import { Menu, Transition } from '@headlessui/react'
import { Fragment, useMemo, useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useDispatch, useSelector } from "react-redux";
import { selectAccessToken, selectIsAuthenticated } from "../../redux/selectors";
import { logoutRequest } from '../../redux/reducers/authSlice';
import { AiOutlinePoweroff } from "react-icons/ai"
import axios from 'axios';

export default function Header() {
    const navigate = useNavigate()
    const location = useLocation()
    const handleNavigate = (url) => {
        navigate(url)
    }
    const user = useSelector(state => state.auth.user)
    const access_token = useSelector(selectAccessToken)
    const isAuthenticated = useSelector(selectIsAuthenticated)
    const dispatch = useDispatch()
    const logout = () => {
        dispatch(logoutRequest(access_token))
    }
    const [departmentname, setDepartmentname] = useState("")
    useEffect(() => {
        axios.get(`${process.env.REACT_APP_API_ENDPOINT}/department/${user.departmentId}`).then((response) => {
            setDepartmentname(response.data.name);
        }).catch((error) => console.error('Error fetching data:', error));
    }, [])
    useEffect(() => {
        console.log(user)
        if (isAuthenticated === false) {
            toast.success('Đăng xuất thành công')
            navigate('/')
        }
    }, [isAuthenticated]) // eslint-disable-line react-hooks/exhaustive-deps
    const active = useMemo(() => "!text-blue-400 after:!w-[80%]", [])
    return (
        <div className="fixed top-0 w-full bg-white shadow-md z-[50]">
            <div className="container flex flex-row h-[100px] py-[15px] items-center">
                <div className="w-1/4 text-xl font-bold text-blue-400">
                    Documentations Management
                </div>
                <div className="flex flex-row justify-between w-3/4 gap-4">
                    <div className="w-1/6"></div>
                    <div className="flex items-center w-3/6 gap-6">
                        <div onClick={() => handleNavigate("/dashboard")} className={`relative after:absolute after:bottom-0 inline-flex w-fit justify-center rounded-md px-4 py-2 text-md focus:outline-none text-black after:w-0 hover:after:w-[80%] after:bg-blue-400 after:h-[2px] hover:text-blue-400 cursor-pointer after:transition-all after:duration-300 ${location.pathname === "/dashboard" ? active : ""}`}>
                            Dashboard
                        </div>
                        <div onClick={() => handleNavigate("/upload")} className={`relative after:absolute after:bottom-0 inline-flex w-fit justify-center rounded-md px-4 py-2 text-md focus:outline-none text-black after:w-0 hover:after:w-[80%] after:bg-blue-400 after:h-[2px] hover:text-blue-400 cursor-pointer after:transition-all after:duration-300 ${location.pathname === "/upload" ? active : ""}`}>
                            Upload
                        </div>
                    </div>
                    <Menu as="div" className="relative inline-block w-2/6 text-left">
                        <div className="flex items-center justify-end gap-4">
                            <div>
                                {departmentname}
                            </div>
                            <Menu.Button className="inline-flex w-fit justify-center rounded-md px-4 py-2 text-md focus:outline-none text-black after:w-0 hover:after:w-full after:bg-blue-400 after:h-[2px] transition-all duration-300">
                                <img className="w-[60px] h-[60px]" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTcZsL6PVn0SNiabAKz7js0QknS2ilJam19QQ&usqp=CAU" alt="avatar"></img>
                            </Menu.Button>
                            <div>
                                {user?.userName}
                            </div>
                        </div>
                        <Transition
                            as={Fragment}
                            enter="transition ease-out duration-100"
                            enterFrom="transform opacity-0 scale-95"
                            enterTo="transform opacity-100 scale-100"
                            leave="transition ease-in duration-75"
                            leaveFrom="transform opacity-100 scale-100"
                            leaveTo="transform opacity-0 scale-95"
                        >
                            <Menu.Items className="absolute right-0 w-56 mt-2 origin-top-right bg-white divide-y divide-gray-100 rounded-md shadow-lg ring-1 ring-black/5 focus:outline-none">
                                <div className="px-1 py-1 ">
                                    <Menu.Item>
                                        {({ active }) => (
                                            <button
                                                onClick={logout}
                                                className={`${active ? 'bg-violet-500 text-white' : 'text-gray-900'
                                                    } group flex w-full items-center rounded-md px-2 py-2 text-sm`}
                                            >
                                                <AiOutlinePoweroff className="mr-3" />
                                                Log out
                                            </button>
                                        )}
                                    </Menu.Item>
                                </div>
                            </Menu.Items>
                        </Transition>
                    </Menu>
                </div>

            </div>
        </div>
    )
}