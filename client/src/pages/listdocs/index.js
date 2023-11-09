import { useState, useEffect } from 'react'
import { useSelector } from 'react-redux';
import './index.css'
import SearchDoc from '../../components/searchDoc'
import { useNavigate } from "react-router-dom";

export default function ListDocs() {
    const [documents, setDocuments] = useState([]);
    const user = useSelector((state) => state.auth.user);
    const navigate = useNavigate();
    useEffect(() => {
        fetch(`${process.env.REACT_APP_API_ENDPOINT}/document/departments/${user.departmentId}`)
            .then((response) => response.json())
            .then((data) => setDocuments(data.listContent))
            .catch((error) => console.error('Error fetching data:', error));
    }, []);

    return (
        <div className="list-docs">
            <div className='text-center'>
                <h1 className=" font-bold mt-10 mb-10 text-4xl content">
                    Government Office <br /> Documents
                </h1>
            </div>
            <div className="flex justify-center">
                <SearchDoc setDocuments={setDocuments} departmentId={user.departmentId} />
            </div>
            <div className=' flex justify-center'>
                <table className="table-auto w-4/5 text-center">
                    <thead className="bg-blue-400">
                        <tr className="">
                            <th >ID</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Lastest Version</th>
                            <th>Download</th>
                            <th>Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        {documents.length > 0 ? (
                            documents.map((document, index) => (
                                <tr>
                                    <td>{index + 1}</td>
                                    <td className="cursor-pointer text-blue-500" onClick={() => {
                                        navigate(`/document/${document.id}`)
                                    }}>
                                        {document?.name}
                                    </td>
                                    <td>{document?.description}</td>
                                    <td>{document?.versions[0]?.name}</td>
                                    <td><a target='__blank' className="text-blue-500 underline" href={document?.versions[0]?.url}>Download link</a></td>
                                    <td>{document?.createTime}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan={6}>No documents found</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    )
}