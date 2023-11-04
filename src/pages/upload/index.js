import { useState } from 'react';
import { ref, uploadBytesResumable, getDownloadURL } from 'firebase/storage';
import { storage } from '../../firebase';

const UploadPage = () => {
    const [progress, setProgress] = useState(0);
    const formHandler = (e) => {
        e.preventDefault();
        const file = e.target[0].files[0];
        uploadFiles(file);
    };

    const uploadFiles = (file) => {
        if (!file) return;

        // Create a storage reference from our storage service
        const storageRef = ref(storage, `files/${file.name}`);
        const uploadTask = uploadBytesResumable(storageRef, file);

        uploadTask.on(
            'state_changed',
            (snapshot) => {
                const prog = Math.round(
                    (snapshot.bytesTransferred / snapshot.totalBytes) * 100
                );
                setProgress(prog);
            },
            (error) => console.error(error),
            () => {
                getDownloadURL(uploadTask.snapshot.ref).then((url) => {
                    console.log('File available at:', url);
                    const token = url.split('?').pop().split('=').pop();
                    console.log('Download token:', token);
                });
            }
        );
    };

    return (
        <div className="App">
            <form onSubmit={formHandler}>
                <input type="file" className="input" />
                <button type="submit">Upload</button>
            </form>
            <hr />
            <h2 className="text-4xl font-bold">Uploading done {progress}%</h2>
        </div>
    );
}

export default UploadPage