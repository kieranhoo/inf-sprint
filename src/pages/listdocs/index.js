export default function ListDocs() {
    return (
        <div className="list-docs">
            <div className='text-center'>
                <h1 className=" font-bold mt-10 mb-10 text-4xl">Documents</h1>
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
                <table class="table-auto w-4/5 text-center">
                    <thead className="bg-blue-400">
                        <tr className="">
                            <th >ID</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Version</th>
                            <th>Link</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1961</td>
                            <td>Malcolm Lockyer</td>
                            <td>The Sliding Mr. Bones (Next Stop, Pottersville)</td>
                            <td>1.0.0</td>
                            <td>Link</td>
                        </tr>
                        <tr>
                            <td>1972</td>
                            <td>Witchy Woman</td>
                            <td>The Eagles</td>
                            <td>1.0.0</td>
                            <td>Link</td>
                        </tr>
                        <tr>
                            <td>1975</td>
                            <td>Shining Star</td>
                            <td>Earth, Wind, and Fire</td>
                            <td>1.0.0</td>
                            <td>Link</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    )
}