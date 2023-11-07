import { useState } from 'react'
import DeleteModal from '../../components/modal/delete.modal';
import './index.css'

export default function ListDocs() {
    const [open, setOpen] = useState(false);
    const [doc, setDoc] = useState(null);

    const handlerDelete = (docData) => {
        setOpen(true);
        setDoc(docData);
    } 

    return (
        <div className="list-docs">
            <div className='text-center'>
                <h1 className=" font-bold mt-10 mb-10 text-4xl content">
                    Government Office <br /> Documents
                </h1>
            </div>
            <div className=' flex justify-center'>
                {/* <table className=" w-4/5 text-center">
                    <tr className=" w-full">
                        <td className=" w-1/5">ID</td>
                        <td className=" w-1/5">Name</td>
                        <td className=" w-1/5">Decs</td>
                        <td className=" w-1/5">Ver</td>
                    </tr>
                </table> */}
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
                        <tr>
                            <td>1961</td>
                            <td>Malcolm Lockyer</td>
                            <td>The Sliding Mr. Bones (Next Stop, Pottersville)</td>
                            <td>1.0.0</td>
                            <td>Link</td>
                            <td></td>
                            <td><button className='bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded' onClick={() => handlerDelete({id:1, nameDocument: 'hello'})}>Delete</button></td>
                        </tr>
                        <tr>
                            <td>1972</td>
                            <td>Witchy Woman</td>
                            <td>The Eagles</td>
                            <td>1.0.0</td>
                            <td>Link</td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>1975</td>
                            <td>Shining Star</td>
                            <td>Earth, Wind, and Fire</td>
                            <td>1.0.0</td>
                            <td>Link</td>
                            <td></td>
                            <td></td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <DeleteModal docData={doc} open={open} onClose = {() => setOpen(false)}>
                <button className="text-gray-500 bg-gray-100 hover:bg-gray-200 font-bold py-2 px-4 rounded" onClick={() => setOpen(false)}>Cancle</button>
            </DeleteModal>
        </div>
    )
}