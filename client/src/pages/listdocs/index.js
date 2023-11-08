import { useState, useEffect } from 'react'
import DeleteModal from '../../components/modal/delete.modal';
import UpdateModal from '../../components/modal/update.modal';
import { useSelector } from 'react-redux';
import './index.css'

export default function ListDocs() {
    const [deleteModalStatus, setDeleteModalStatus] = useState(false);
    const [updateModalStatus, setUpdateModalStatus] = useState(false);
    const [doc, setDoc] = useState('');
    const [documents, setDocuments] = useState([]);
    const user = useSelector((state) => state.auth.user);

    useEffect(() => {
        // Use the Fetch API to fetch data from the API endpoint
        fetch(`${process.env.REACT_APP_API_ENDPOINT}/document/departments/${user.departmentId}`)
        .then((response) => response.json())
        .then((data) => setDocuments(data.listContent))
        .catch((error) => console.error('Error fetching data:', error));
    }, []);

    const handlerDeleteModalOpen = (status) => {
        setDeleteModalStatus(status);
    }
    const handlerDelete = (docData) => {
        setDoc(docData);
        setDeleteModalStatus(true);
    } 

    const handlerUpdateModalOpen = (status) => {
        setUpdateModalStatus(status);
    }
    const handlerUpdate = (docData) => {
        setDoc(docData);
        setUpdateModalStatus(true);
    } 

    return (
        <div className="list-docs">
            <div className='text-center'>
                <h1 className=" font-bold mt-10 mb-10 text-4xl content">
                    Government Office <br /> Documents
                </h1>
            </div>
            <div className=' flex justify-center'>
                <table className="table-auto w-4/5 text-center">
                    <thead className="bg-blue-400">
                        <tr className="">
                            <th >ID</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Version</th>
                            <th>Download</th>
                            <th>Time</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {documents.map((document) => (
                        <tr>
                            <td>{document.id}</td>
                            <td>{document.nameDocument}</td>
                            <td>{document.description}</td>
                            <td>{document.nameVersion}</td>
                            <td>{document.url}</td>
                            <td>{document.createTime}</td>
                            <td>
                                <button className='bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded' onClick={() => handlerDelete(document)}>Delete</button>
                                <button className='bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded' onClick={() => handlerUpdate(document)}>Update</button>
                            </td>
                        </tr>))}
                    </tbody>
                </table>
            </div>

            <DeleteModal docData={doc} sendOpenStatusToParent={handlerDeleteModalOpen} open={deleteModalStatus} onClose = {() => setDeleteModalStatus(false)}>
            </DeleteModal>
            <UpdateModal docData={doc} sendOpenStatusToParent={handlerUpdateModalOpen} open={updateModalStatus} onClose = {() => setUpdateModalStatus(false)}>
            </UpdateModal>
        </div>
    )
}