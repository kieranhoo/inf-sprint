import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function DeleteModal ({docId, sendOpenStatusToParent, open, onClose}) {
    const navigate = useNavigate();

    const deleteDocumentById = async (id) => {
        try {
            await axios.delete(`${process.env.REACT_APP_API_ENDPOINT}/document/${id}`);
            sendOpenStatusToParent(false);
            navigate('/dashboard');
        } catch (error) {
            console.error(`Error deleting data with ID ${id}:`, error);
        }
    }

    return (
        <div onClick={onClose} 
            className={`
            fixed inset-0 flex justify-center items-center 
            transition-colors 
            ${open ? 'visible bg-black/20' : 'invisible'}`}
        >
            <div onClick={(e) => e.stopPropagation()}
                className={`
                bg-white rounded-xl shadow p-6 transition-all 
                ${open ? "scale-100 opacity-100" : "scale-125 opacity-0"}`}
            >
                <div className="text-center w-56">
                    <div className="mx-auto my-4 w-48">
                        <h3 className=" text-lg font-black text-gray-800">Confirm Delete</h3>
                        <p className=" text-sm text-gray-500">Are you sure you want delete this document?</p>
                    </div>
                    <div className="justify-around flex gap-4">
                        <button className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded" onClick={() => deleteDocumentById(docId)}>Delete</button>
                        <button className="text-gray-500 bg-gray-100 hover:bg-gray-200 font-bold py-2 px-4 rounded" onClick={() => sendOpenStatusToParent(false)}>Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    )
}