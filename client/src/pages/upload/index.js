import { useState } from 'react';
import { ref, uploadBytesResumable, getDownloadURL } from 'firebase/storage';
import { storage } from '../../firebase';
import axios from 'axios';

const UploadPage = () => {
    const [progress, setProgress] = useState(0);
    const [title, setTitle] = useState();
    const [version, setVersion] = useState("1.0.0");
    const [description, setDescription] = useState();
    const [message, setMessage] = useState();
    const [note, setNote] = useState();
    const [isProcessing, setIsProcessing] = useState(false);

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
                departmentId: "1",
                nameVersion: version,
            })
            setTitle("");
            setVersion("");
            setDescription("");
            setMessage("Document upload successfully");
            setIsProcessing(false);
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
        <div className="App">

            <section className="max-w-4xl p-6 mx-auto bg-indigo-500 rounded-md shadow-md dark:bg-gray-800 mt-20">
                <h1 className="text-xl font-bold text-white capitalize dark:text-white">Upload new document</h1>
                <form onSubmit={formHandler}>
                    <div className="mt-4">
                        <div>
                            <label className="text-white dark:text-gray-200" htmlFor="title">Title</label>
                            <input id="title" type="text" onChange={(e) => setTitle(e.target.value)} class="block w-full px-4 py-2 mt-2 text-gray-700 bg-white border border-gray-300 rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-500 dark:focus:border-blue-500 focus:outline-none focus:ring" />
                        </div>
                    </div>
                    <br />
                    <div>
                        <label className="text-white dark:text-gray-200" htmlFor="description">Description</label>
                        <textarea id="description" type="textarea" onChange={(e) => setDescription(e.target.value)} className="block w-full px-4 py-2 mt-2 text-gray-700 bg-white border border-gray-300 rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-500 dark:focus:border-blue-500 focus:outline-none focus:ring"></textarea>
                    </div>
                    <br />
                    <div>
                        <label className="text-white dark:text-gray-200" htmlFor="note">Note</label>
                        <textarea id="note" type="textarea" onChange={(e) => setNote(e.target.value)} className="block w-full px-4 py-2 mt-2 text-gray-700 bg-white border border-gray-300 rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-500 dark:focus:border-blue-500 focus:outline-none focus:ring"></textarea>
                    </div>
                    <br />
                    <div>
                        <label className="block text-sm font-medium text-white">
                            File
                        </label>
                        <div className="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-md">
                            <div className="space-y-1 text-center">
                                <svg className="mx-auto h-12 w-12 text-white" stroke="currentColor" fill="none" viewBox="0 0 48 48" aria-hidden="true">
                                    <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <div className="flex text-sm text-gray-600">
                                    <input id="file-upload" name="file-upload" type="file" className="block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 dark:text-gray-400 focus:outline-none dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400" multiple />
                                </div>
                            </div>
                        </div>
                    </div>

                    <br />
                    {isProcessing ? <h2 className='text-white'>Uploading done {progress}%</h2>
                        : null}
                    <div className="message text-white">{progress === 100 ? <p>{message}</p> : null}</div>

                    <div className="flex justify-end mt-6">
                        <button className="px-6 py-2 leading-5 text-white transition-colors duration-200 transform bg-pink-500 rounded-md hover:bg-pink-700 focus:outline-none focus:bg-gray-600">Save</button>
                    </div>
                </form>
            </section>
        </div>
    );
}

export default UploadPage