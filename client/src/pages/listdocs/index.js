import { useState, useEffect } from 'react'
import { useSelector } from 'react-redux';
import './index.css'
import SearchDoc from '../../components/searchDoc'
import { useNavigate } from "react-router-dom";
import ReactPaginate from 'react-paginate';
import { Skeleton } from '../../components/skeleton';
import { toast } from "react-toastify"

export default function ListDocs() {
    const [documents, setDocuments] = useState([]);
    const [curdocs, setCurDocs] = useState([])
    const user = useSelector((state) => state.auth.user);
    const navigate = useNavigate();
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [error, setError] = useState(false)
    const itemsPerPage = 10;

    useEffect(() => {
        fetch(`${process.env.REACT_APP_API_ENDPOINT}/document/departments/${user?.departmentId}`)
            .then((response) => response.json())
            .then((data) => {
                if (data.listContent.length === 0) {
                    toast.error('Không có data')
                }
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
        setCurDocs(documents.slice(startIndex, endIndex))
        setTotalPages(Math.ceil((documents.length) / itemsPerPage))
    }, [currentPage, documents])


    return (
        <div className="list-docs">
            <div className='text-center'>
                <h1 className="mt-10 mb-10 text-4xl font-bold content">
                    Government Office <br /> Documents
                </h1>
            </div>
            <div className='flex flex-col items-center justify-center w-full gap-12'>
                <div className="flex justify-center w-full">
                    <SearchDoc setDocuments={setDocuments} departmentId={user?.departmentId} setError={setError} />
                </div>
                <div className='flex flex-col items-center justify-center w-full gap-12 '>
                    <table className="w-4/5 text-center border border-black table-auto">
                        <thead className="bg-blue-400">
                            <tr className="">
                                <th className="border border-black" >ID</th>
                                <th className="border border-black">Name</th>
                                <th className="border border-black">Description</th>
                                <th className="border border-black">Lastest Version</th>
                                <th className="border border-black">Download</th>
                                <th className="border border-black"> Time</th>
                            </tr>
                        </thead>
                        <tbody>
                            {!error && curdocs.length > 0 ? (
                                curdocs.map((document, index) => (
                                    <tr key={index}>
                                        <td className="border border-black">{index + 1}</td>
                                        <td className="text-blue-500 border border-black cursor-pointer" onClick={() => {
                                            navigate(`/document/${document.id}`)
                                        }}>
                                            {document?.name}
                                        </td>
                                        <td className="border border-black">{document?.description}</td>
                                        <td className="border border-black">{document?.versions[0]?.name}</td>
                                        <td className="border border-black"><a target='__blank' className="text-blue-500 underline" href={document?.versions[0]?.url}>Download link</a></td>
                                        <td className="border border-black">{document?.createTime}</td>
                                    </tr>
                                ))
                            ) : (
                                !error && <Skeleton />
                            )}
                        </tbody>
                    </table>
                    {
                        error ? <div className="flex w-full justify-center">
                            No documents found
                        </div> : ""
                    }
                    {
                        curdocs?.length === 0 || error ? "" :
                            <ReactPaginate
                                containerClassName={"flex flex-row gap-4"}
                                pageClassName={"li-pagination rounded-[5px] bg-gray-400 rounded-[5px] transition duration-500"}
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