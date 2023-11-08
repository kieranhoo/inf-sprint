import { useState } from 'react';
import { ref, uploadBytesResumable, getDownloadURL } from 'firebase/storage';
import { storage } from '../../firebase';
import axios from 'axios';

export default function UpdateModal({ docData, sendOpenStatusToParent, open, onClose }) {
    const [progress, setProgress] = useState(0);
    const [title, setTitle] = useState(docData.title);
    const [version] = useState(docData.version);
    const [description, setDescription] = useState(docData.description);
    const [message, setMessage] = useState();
    const [note, setNote] = useState(docData.note);
    const [isProcessing, setIsProcessing] = useState(false);
    const [isUpdateNewVersion, setIsUpdateNewVersion] = useState(false);

    const formHandler = async (e) => {
        e.preventDefault();
        const file = document.getElementById("file-upload").files[0];
        uploadFiles(file);
    };

    const updateDocumentById = async (url) => {
        try {
            if (url != null) {
                const versionArr = version.split('.');
                const newVersion = `${versionArr[0]}.${versionArr[1]}.${parseInt(versionArr[2]) + 1}`;
                await axios.put(`${process.env.REACT_APP_API_ENDPOINT}/document/${docData.id}`, {
                    nameDocument: title,
                    description: description,
                    url,
                    note,
                    nameVersion: newVersion,
                })
            } else {
                await axios.put(`${process.env.REACT_APP_API_ENDPOINT}/document/${docData.id}`, {
                    nameDocument: title,
                    description: description,
                })
            }
            setTitle("");
            setDescription("");
            setNote("");
            setMessage("Document update successfully");
            setIsProcessing(false);
            sendOpenStatusToParent(false);
        } catch (err) {
            setMessage("Something went wrong");
        }
    }

    const uploadFiles = (file) => {
        if (!file) updateDocumentById(null);
        else {
            setIsProcessing(true);
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
                        await updateDocumentById(geturl);
                    });
                }
            );
        }
    };

    return (
        <div onClick={onClose}
            className={`
            fixed inset-0 flex justify-center items-center 
            transition-colors 
            ${open ? 'visible bg-black/20' : 'invisible'}`}
        >
            <div onClick={(e) => e.stopPropagation()}
                className={`
                bg-white w-1/3 rounded-xl shadow transition-all 
                ${open ? "scale-100 opacity-100" : "scale-125 opacity-0"}`}
            >
                <form class="bg-white shadow-md rounded-xl px-8 pt-6 pb-8 w-full">
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="nameDocument">
                            Document Name
                        </label>
                        <input defaultValue={title} onChange={(e) => setTitle(e.target.value)} class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="nameDocument" type="text" placeholder="nameDocument" />
                    </div>
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="description">
                            Description
                        </label>
                        <input defaultValue={description} onChange={(e) => setDescription(e.target.value)} class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="description" type="textarea" placeholder="description" />
                    </div>
                    <div class="mb-6">
                        <label className="block text-gray-700 text-sm font-bold mb-2">
                            File
                        </label>
                        <div className="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-md">
                            <div className="space-y-1 text-center">
                                <svg className="mx-auto h-12 w-12 text-gray-700" stroke="currentColor" fill="none" viewBox="0 0 48 48" aria-hidden="true">
                                    <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <div className="flex text-sm text-gray-600">
                                    <input onChange={(e) => {
                                        if (e.target.files.length > 0) {
                                            setIsUpdateNewVersion(true);
                                        }
                                    }}
                                        id="file-upload" name="file-upload" type="file" className="block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 dark:text-gray-400 focus:outline-none dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400" multiple />
                                </div>
                                <p>Choose another file if you want to update new version!</p>
                            </div>
                        </div>
                        {isUpdateNewVersion ? <>
                            <div class="mb-4">
                                <label class="block text-gray-700 text-sm font-bold mb-2" for="note">
                                    Note
                                </label>
                                <input defaultValue={note} onChange={(e) => setNote(e.target.value)} class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="note" type="textarea" placeholder="note" />
                            </div>
                        </> : null}
                        <div className='flex justify-center'>
                            <div>
                                {isProcessing ? <h2 className='text-gray-700'>Uploading done {progress}%</h2>
                                    : null}
                                <div className="message text-gray-700">{progress === 100 ? <p>{message}</p> : null}</div>
                            </div>
                        </div>
                    </div>
                    <div className='justify-center flex'>
                        <div className="justify-around flex gap-4 w-56">
                            <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" onClick={formHandler}>Update</button>
                            <button className="text-gray-500 bg-gray-100 hover:bg-gray-200 font-bold py-2 px-4 rounded" onClick={() => sendOpenStatusToParent(false)}>Cancel</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    )
}


