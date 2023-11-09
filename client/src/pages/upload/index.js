import { useState } from 'react';
import { ref, uploadBytesResumable, getDownloadURL } from 'firebase/storage';
import { storage } from '../../firebase';
import axios from 'axios';
import { useSelector } from 'react-redux';
import {useNavigate} from 'react-router-dom'

const UploadPage = () => {
    const [progress, setProgress] = useState(0);
    const [title, setTitle] = useState();
    const [version, setVersion] = useState("1.0.0");
    const [description, setDescription] = useState();
    const [message, setMessage] = useState();
    const [note, setNote] = useState();
    const [isProcessing, setIsProcessing] = useState(false);
    const user = useSelector((state) => state.auth.user);
    const navigate = useNavigate();

    const formHandler = async (e) => {
        e.preventDefault();
        setIsProcessing(true);
        const file = document.getElementById("file-upload").files[0];
        uploadFiles(file);
    };

    const uploadFilesToDD = async (url) => {
        try {
            await axios.post(`${process.env.REACT_APP_API_ENDPOINT}/document`, {
                nameDocument: title,
                description: description,
                url,
                note,
                departmentId: user.departmentId,
                nameVersion: version,
            })
            setTitle("");
            setVersion("");
            setDescription("");
            setMessage("Document upload successfully");
            setIsProcessing(false);
            navigate('/dashboard');
        } catch (err) {
            setMessage("Something went wrong");
        }
    }

    const uploadFiles = (file) => {
        if (!file) return;

        const storageRef = ref(storage, `files/${file.name}`);
        const uploadTask = uploadBytesResumable(storageRef, file);

        uploadTask.on(
            'state_changed',
            (snapshot) => {
                const prog = Math.round(
                    (snapshot.bytesTransferred / snapshot.totalBytes) * 100
                );
                setProgress(prog);
            },
            (error) => console.error(error),
            () => {
                getDownloadURL(uploadTask.snapshot.ref).then(async (geturl) => {
                    await uploadFilesToDD(geturl);
                });
            }
        );
    };

    return (
        <div className="App flex justify-center items-center">

            <section className="bg-white w-1/3 rounded-xl shadow">
                <h1 className="text-3xl mt-8 font-bold text-blue-500 capitalize dark:text-white">Upload new document</h1>
                <form onSubmit={formHandler} className="bg-white shadow-md rounded-xl px-8 pt-3 pb-8 w-full">
                    <div class="mb-4">
                        <label class="flex text-gray-700 text-sm font-bold mb-2" for="title">
                            Title
                        </label>
                        <input id="title" type="text" placeholder="title" onChange={(e) => setTitle(e.target.value)} class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
                    </div>
                    <div class="mb-4">
                        <label class="flex text-gray-700 text-sm font-bold mb-2" for="description">
                            Description
                        </label>
                        <input id="description" type="textarea" placeholder="description" onChange={(e) => setDescription(e.target.value)} class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
                    </div>
                    <div class="mb-4">
                        <label class="flex text-gray-700 text-sm font-bold mb-2" for="description">
                            Note
                        </label>
                        <input id="note" type="textarea" placeholder="note" onChange={(e) => setNote(e.target.value)} class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"/>
                    </div>
                    <div class="mb-6">
                        <label className="flex text-gray-700 text-sm font-bold mb-2">
                            File
                        </label>
                        <div className="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-md">
                            <div className="space-y-1 text-center">
                                <svg className="mx-auto h-12 w-12 text-gray-700" stroke="currentColor" fill="none" viewBox="0 0 48 48" aria-hidden="true">
                                    <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <div className="flex text-sm text-gray-600">
                                    <input id="file-upload" name="file-upload" type="file" className="block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 dark:text-gray-400 focus:outline-none dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400" multiple />
                                </div>
                            </div>
                        </div>
                        {isProcessing ? <h2 className='text-gray-700'>Uploading done {progress}%</h2>
                            : null}
                        <div className="message text-gray-700">{progress === 100 ? <p>{message}</p> : null}</div>
                    </div>  

                    <div className='justify-center flex'>
                            <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Save</button>                        
                    </div>
                </form>
            </section>
        </div>
    );
}

export default UploadPage