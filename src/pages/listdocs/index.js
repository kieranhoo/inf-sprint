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
                <table class="table-auto">
                    <thead>
                        <tr>
                            <th>Song</th>
                            <th>Artist</th>
                            <th>Year</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>The Sliding Mr. Bones (Next Stop, Pottersville)</td>
                            <td>Malcolm Lockyer</td>
                            <td>1961</td>
                        </tr>
                        <tr>
                            <td>Witchy Woman</td>
                            <td>The Eagles</td>
                            <td>1972</td>
                        </tr>
                        <tr>
                            <td>Shining Star</td>
                            <td>Earth, Wind, and Fire</td>
                            <td>1975</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    )
}