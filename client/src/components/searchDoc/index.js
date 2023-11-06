import React, { useState } from 'react';
export default function SearchDoc() {
    const [searchTerm, setSearchTerm] = useState('');
    const submit = () => {

    }
    return (
        <div className="mb-4">
            <label className="block text-sm font-medium text-gray-600 mb-1">Search by Title:</label>
            <input
                type="text"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="border border-gray-300 p-2 rounded-md w-full"
                placeholder="Enter title to search..."
            />
            <button onClick={submit} className="text-white font-bold transition-all duration-300 -translate-y-2 bg-blue-400 p-3 rounded-md mt-4">Search</button>
        </div>
    )
}