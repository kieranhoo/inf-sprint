import { useState, useEffect } from 'react'
import { useSelector } from 'react-redux';
import './index.css'
import SearchDoc from '../../components/searchDoc'
import { useNavigate } from "react-router-dom";
import ReactPaginate from 'react-paginate';
import { Skeleton } from '../../components/skeleton';

export default function ListDocs() {
    const [documents, setDocuments] = useState([]);
    const [curdocs, setCurDocs] = useState([])
    const user = useSelector((state) => state.auth.user);
    const navigate = useNavigate();
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const itemsPerPage = 10;

    useEffect(() => {
        fetch(`${process.env.REACT_APP_API_ENDPOINT}/document/departments/${user.departmentId}`)
            .then((response) => response.json())
            .then((data) => {
                setDocuments(data.listContent)
                setTotalPages(Math.ceil((data.listContent.length) / itemsPerPage))
                setCurDocs(data.listContent.slice(0, 9))
            })
            .catch((error) => console.error('Error fetching data:', error));
    }, []);
    const handlePageChange = (newPage) => {
        setCurrentPage(newPage.selected + 1);
    };
    useEffect(() => {
        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        // console.log(documents.slice(startIndex, endIndex), documents, startIndex, endIndex)
        setCurDocs(documents.slice(startIndex, endIndex))
    }, [currentPage, documents])

    return (
        <div className="list-docs">
            <div className='text-center'>
                <h1 className=" font-bold mt-10 mb-10 text-4xl content">
                    Government Office <br /> Documents
                </h1>
            </div>
            <div className=' flex justify-center flex-col items-center gap-12'>
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
                            {curdocs.length > 0 ? (
                                curdocs.map((document, index) => (
                                    <tr key={index}>
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
                                <Skeleton />
                            )}
                        </tbody>
                    </table>
                    {
                        curdocs?.length === 0 ? "" :
                            <ReactPaginate
                                containerClassName={"flex flex-row gap-4"}
                                pageClassName={"px-3 px-3 py-1 rounded-[5px] bg-gray-400 py-1 rounded-[5px] transition duration-500"}
                                activeClassName={"text-black !bg-blue-400 !text-white"}
                                breakLabel="..."
                                onPageChange={handlePageChange}
                                pageRangeDisplayed={itemsPerPage}
                                pageCount={totalPages}
                                nextLabel=">"
                                previousLabel="<"
                            />
                    }
                </div>
            </div>
        </div>
    )
}