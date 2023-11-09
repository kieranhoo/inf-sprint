import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';


import DeleteModal from '../../components/modal/delete.modal';
import UpdateModal from '../../components/modal/update.modal';


export const DocDetail = () => {
    const { id } = useParams();
    const [deleteModalStatus, setDeleteModalStatus] = useState(false);
    const [updateModalStatus, setUpdateModalStatus] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [documents, setDocuments] = useState([]);
    const [selectedVersion, setSelectedVersion] = useState();

    const handlerDeleteModalOpen = (status) => {
        setDeleteModalStatus(status);
    }
    const handlerUpdateModalOpen = (status) => {
        setUpdateModalStatus(status);
    }

    const handleVersionChange = (event) => {
        const selectedDocument = documents.find((document) => document.version === event.target.value);
        setSelectedVersion(selectedDocument);
    };

    useEffect(() => {
        const fetchDocuments = async () => {
            const { data } = await axios.get(`${process.env.REACT_APP_API_ENDPOINT}/document/${id}`);
            let { versions } = data;
            versions = versions.map((version) => {
                const ret = {
                    id: data.id,
                    title: data.name,
                    description: data.description,
                    version: version.name,
                    time: version.updateTime,
                    file: version.url,
                    department: data.department.name,
                    currentVersion: version.currentVersion,
                    note: version.note,
                    versionId: version.id,
                };
                if (version.currentVersion) {
                    setSelectedVersion(ret);
                }
                return ret;
            })
            versions.sort((a, b) => {
                if (a.versionId < b.versionId) {
                    return 1;
                }
                if (a.versionId > b.versionId) {
                    return -1;
                }
                return 0;
            });
            setDocuments(versions);
            setIsLoading(false);
        };
        fetchDocuments();
    }, [id]);

    return (
        <div className="pt-6 container">
            <h1 className="text-4xl font-bold mb-4 text-blue-400 text-center">Document Detail</h1>
            {isLoading ? (
                <div>Loading...</div>
            ) : (
                <><div className="mb-4">
                    <label className="block text-sm font-medium text-gray-600 mb-1">Select Version:</label>
                    <select className="border border-gray-300 p-2 rounded-md w-full" onChange={handleVersionChange}>
                        {documents.map((document) => {
                            if (document.currentVersion) {
                                return <option value={document.version} key={document.version} selected>{document.version}</option>
                            }
                            return <option value={document.version} key={document.version}>{document.version}</option>
                        })}
                    </select>
                </div>
                    <div className="shadow-md p-5 rounded-md bg-white">
                        {selectedVersion ? (
                            <div className="flex flex-col gap-4">
                                <h2 className="text-xl font-bold mb-2 text-blue-400">Title: {selectedVersion.title}</h2>
                                <h4 className="text-md font-bold mb-2 text-blue-400">Document ID: {selectedVersion.id}</h4>
                                <div className="mb-2">Created Time: {selectedVersion.time}</div>
                                <div className="mb-2">File: <a href={selectedVersion.file} download className="text-blue-500">{selectedVersion.file}</a></div>
                                <div className="mb-2">Description:</div>
                                <div className="mb-2">{selectedVersion.description}</div>
                                <div className="mb-2">Department: {selectedVersion.department}</div>
                                {selectedVersion.note && (
                                    <>
                                        <div className="mb-2">Note:</div>
                                        <div className="mb-2">{selectedVersion.note}</div>
                                    </>
                                )}
                                <div className='flex justify-center'>
                                    <div className='flex justify-around w-56'>
                                        <button className='bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded' onClick={() => setUpdateModalStatus(true)}>Update</button>
                                        <button className='bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded' onClick={() => setDeleteModalStatus(true)}>Delete</button>
                                    </div>  
                                </div>
                            </div>
                        ) : (
                            <div className="text-center">No versions found</div>
                        )}
                    </div>   
                </>
            )}
            
            {selectedVersion && <DeleteModal docId={id} sendOpenStatusToParent={handlerDeleteModalOpen} open={deleteModalStatus} onClose = {() => setDeleteModalStatus(false)}>
            </DeleteModal>}
            {selectedVersion && <UpdateModal docData={selectedVersion} sendOpenStatusToParent={handlerUpdateModalOpen} open={updateModalStatus} onClose = {() => setUpdateModalStatus(false)}>
            </UpdateModal>}
        </div>
    );
};
