PojoToSQLite is an object database where Java objects are stored in the SQLite database.

<b>Usage:</b>

1. Copy the SQLite folder to your project.

2. Declare which Classes you want to store in the database in the DatabaseHelper's onCreate method:
<pre>
	<div>
	dFlex = new DFlex(db);
	
	dFlex.createTableFromClass(Visit.class.getName());
	
	dFlex.createTableFromClass(User.class.getName());
</div>
</pre>


3. In your project use the DatabaseHelper singleton class to read/write data.

	e.g.
<pre>
    <div>
        DatabaseHelper.getInstance(MainActivity.this).addObjectToTable(lUser, User.class.getName(), true);
    </div>
</pre>

