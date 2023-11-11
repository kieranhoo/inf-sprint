import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function SearchDoc({ setDocuments, departmentId }) {
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            axios.post(`${process.env.REACT_APP_API_ENDPOINT}/document/search`, {
                departmentID: departmentId,
                keyword: searchTerm
            }).then((response) => {
                setDocuments(response.data.listContent);
            }).catch((error) => console.error('Error fetching data:', error));
        }, 500);

        return () => clearTimeout(delayDebounceFn);
    }, [searchTerm]);

    const inputClass = searchTerm ? 'border-blue-500' : 'border-gray-300';

    return (
        <div className="w-4/5 mb-4">
            <label className="block mb-1 text-sm font-medium text-gray-600">Search documents:</label>
            <input
                type="text"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className={`border p-2 rounded-md w-full ${inputClass}`}
                placeholder="Enter title to search..."
            />
        </div>
    )
}