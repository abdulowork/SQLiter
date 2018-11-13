package co.touchlab.sqliter

class NativeStatement(internal val connection: NativeDatabaseConnection, internal val statementPtr:Long):Statement {
    override fun execute() {
        try {
            nativeExecute(connection.connectionPtr, statementPtr)
        } finally {
            reset()
        }
    }

    override fun executeInsert():Long = try {
        nativeExecuteForLastInsertedRowId(connection.connectionPtr, statementPtr)
    } finally {
        reset()
    }

    override fun executeUpdateDelete():Int = try {
        nativeExecuteForChangedRowCount(connection.connectionPtr, statementPtr)
    } finally {
        reset()
    }

    override fun query(): Cursor = NativeCursor(this)

    override fun finalizeStatement() {
        nativeFinalizeStatement(connection.connectionPtr, statementPtr)
    }

    override fun reset() {
        nativeResetStatementAndClearBindings(connection.connectionPtr, statementPtr)
    }

    override fun bindNull(index: Int) {
        nativeBindNull(connection.connectionPtr, statementPtr, index)
    }

    override fun bindLong(index: Int, value: Long) {
        nativeBindLong(connection.connectionPtr, statementPtr, index, value)
    }

    override fun bindDouble(index: Int, value: Double) {
        nativeBindDouble(connection.connectionPtr, statementPtr, index, value)
    }

    override fun bindString(index: Int, value: String) {
        nativeBindString(connection.connectionPtr, statementPtr, index, value)
    }

    override fun bindBlob(index: Int, value: ByteArray) {
        nativeBindBlob(connection.connectionPtr, statementPtr, index, value)
    }

    override fun bindParameterIndex(paramName: String): Int {
        val index = nativeBindParameterIndex(statementPtr, paramName)
        if(index == 0)
            throw IllegalArgumentException("Statement parameter $paramName not found")
        return index
    }

    @SymbolName("Android_Database_SQLiteConnection_nativeExecute")
    private external fun nativeExecute(connectionPtr:Long, statementPtr:Long)

    @SymbolName("Android_Database_SQLiteConnection_nativeExecuteForChangedRowCount")
    private external fun nativeExecuteForChangedRowCount(connectionPtr:Long, statementPtr:Long):Int

    @SymbolName("Android_Database_SQLiteConnection_nativeExecuteForLastInsertedRowId")
    private external fun nativeExecuteForLastInsertedRowId(
        connectionPtr:Long, statementPtr:Long):Long


    @SymbolName("Android_Database_SQLiteConnection_nativeResetStatementAndClearBindings")
    private external fun nativeResetStatementAndClearBindings(
        connectionPtr:Long, statementPtr:Long)

    @SymbolName("Android_Database_SQLiteConnection_nativeBindNull")
    private external fun nativeBindNull(connectionPtr:Long, statementPtr:Long,
                                        index:Int)
    @SymbolName("Android_Database_SQLiteConnection_nativeBindLong")
    private external fun nativeBindLong(connectionPtr:Long, statementPtr:Long,
                                        index:Int, value:Long)
    @SymbolName("Android_Database_SQLiteConnection_nativeBindDouble")
    private external fun nativeBindDouble(connectionPtr:Long, statementPtr:Long,
                                          index:Int, value:Double)
    @SymbolName("Android_Database_SQLiteConnection_nativeBindString")
    private external fun nativeBindString(connectionPtr:Long, statementPtr:Long,
                                          index:Int, value:String)
    @SymbolName("Android_Database_SQLiteConnection_nativeBindBlob")
    private external fun nativeBindBlob(connectionPtr:Long, statementPtr:Long,
                                        index:Int, value:ByteArray)

}

@SymbolName("Android_Database_SQLiteConnection_nativeFinalizeStatement")
private external fun nativeFinalizeStatement(connectionPtr:Long, statementPtr:Long)

@SymbolName("SQLiter_SQLiteConnection_nativeBindParameterIndex")
private external fun nativeBindParameterIndex(statementPtr:Long, paramName:String):Int