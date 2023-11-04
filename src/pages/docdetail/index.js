import React, { useState } from 'react';
import SearchDoc from '../../components/searchDoc';

export const DocDetail = () => {
    const [documents] = useState([
        // Danh sách các phiên bản document, mỗi phiên bản là một đối tượng
        {
            id: 1,
            title: 'Document Title 1',
            description: 'Description for Document 1',
            version: 1,
            time: '2023-11-04',
            file: 'document1.pdf',
            category: 'Category 1',
        },
        {
            id: 2,
            title: 'Document Title 2',
            description: 'Description for Document 2',
            version: 2,
            time: '2023-11-05',
            file: 'document2.pdf',
            category: 'Category 2',
        },
        // Thêm các phiên bản document khác nếu cần
    ]);
    const [selectedVersion, setSelectedVersion] = useState(documents[0]);
    const handleVersionChange = (event) => {
        const selectedId = parseInt(event.target.value);
        const selectedDocument = documents.find((doc) => doc.id === selectedId);
        setSelectedVersion(selectedDocument);
    };

    return (
        <div className="pt-6 container">
            <SearchDoc />
            <h1 className="text-4xl font-bold mb-4 text-blue-400 text-center">Document Detail</h1>
            <div className="mb-4">
                <label className="block text-sm font-medium text-gray-600 mb-1">Select Version:</label>
                <select className="border border-gray-300 p-2 rounded-md w-full" onChange={handleVersionChange}>
                    {documents.map((doc) => (
                        <option key={doc.id} value={doc.id}>
                            Version {doc.version} - {doc.time}
                        </option>
                    ))}
                </select>
            </div>
            <div className="shadow-md p-5 rounded-md bg-white">
                {selectedVersion && (
                    <div className="flex flex-col gap-4">
                        <h2 className="text-xl font-bold mb-2 text-blue-400">Title: {selectedVersion.title}</h2>
                        <h4 className="text-md font-bold mb-2 text-blue-400">Document ID: {selectedVersion.id}</h4>
                        <div className="mb-2">Created Time: {selectedVersion.time}</div>
                        <div className="mb-2">Category: {selectedVersion.category}</div>
                        <div className="mb-2">File: <a href={selectedVersion.file} download className="text-blue-500">{selectedVersion.file}</a></div>
                        <div className="mb-2">Description:</div>
                        <div className="mb-2">{selectedVersion.description}</div>
                    </div>
                )}
            </div>
        </div>
    );
};
