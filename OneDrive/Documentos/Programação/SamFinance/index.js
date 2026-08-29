const express = require('express');
const app = express();
const db = require('./db');
const port = 3000;


app.use(express.json());


app.get('/users', async (req, res) => {
    try {
     const [rows] = await db.query('SELECT * FROM users');
     res.json(rows);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Erro ao conectar no bando de dados: ' + err.message);
    }
    
});


app.get('/categories', async (req, res) => {
    try {
     const [rows] = await db.query('SELECT * FROM categories');
     res.json(rows);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Erro ao conectar no bando de dados: ' + err.message);
    }
    
});

app.get('/transactions', async (req, res) => {
    try { `const sql = 
     SELECT
      t.*,
      c.name AS category_name 
      FROM transactions t
      INNER JOIN categories c ON t.category_id = c.id;`
     const [rows] = await db.query('SELECT * FROM transactions');
     res.json(rows);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Erro ao conectar no bando de dados: ' + err.message);
    }
    
});

app.get('/', (req, res) => {
    res.send('SamFinance Online e pronto para o banco!')
})

app.listen(port, () => {
    console.log(`Servidor rodando em http://localhost:${port}`);
});


app.post('/users', async (req, res) => {
    const { name, email, password} = req.body;

    try {
        const query = 'INSERT INTO users ( name, email, password) VALUES (?, ?, ?)';
        const [result] = await db.query(query, [ name, email, password]);

        res.status(201).json({
            message: 'Usuário cadastrado com sucesso!',
            id: result.insertId
        });
    } catch (err) {
        console.error(err);
        res.status(500).send('Erro ao cadastrar usuário!')
    }
});

app.post('/categories', async (req, res) => {
    const {users_id, name, type} = req.body;

    try {
        const query = 'INSERT INTO categories (users_id, name, type) VALUES (?, ?, ?)';
        const [result] = await db.query(query, [users_id, name, type]);

        res.status(201).json({
            message: 'Categoria cadastrada com sucesso!',
            id: result.insertId
        });
    } catch (err) {
        console.error(err);
        res.status(500).send('Erro ao cadastrar categoria!')
    }
});

app.post('/transactions', async(req, res) => {
    const {users_id, category_id, description, amount, date} = req.body;

    try {
        const query = 'INSERT INTO transactions (users_id, category_id, description, amount, date) VALUES (?, ?, ?, ?, ?)';
        const [result] = await db.query(query, [users_id, category_id, description, amount, date ]);

        res.status(201).json({
            message: 'Transação realizada!',
            id: result.insertId
        }); 
    } catch (err) {
        console.error(err);
        res.status(500).send('Transação não realizada, tente novamente mais tarde')
    }
});

app.delete('/users/:id', async (req, res) => {
    const {id} = req.params;

    try {
        const query = 'DELETE FROM users WHERE id = ?';
        const [result] = await db.query(query, [id]);

        if (result.affectedRows === 0) {
            return res.status(404).send('Usuário não encontrado');
        }
        res.json({message: 'Usuário deletado com sucesso'});
    } catch (err) {
        console.error(err);
        res.status(500).send('Erro ao deletar usuário');
    }
});

app.delete('/categories/:id', async (req, res) => {
    const {id} = req.params;

    try {
        const query = 'DELETE FROM categories WHERE id = ?';
        const [result] = await db.query(query, [id]);

        if (result.affectedRows === 0) {
            return res.status(404).send('Categoria não encontrada');
        }
        res.json({message: 'Categoria deletado com sucesso'});
    } catch (err) {
        console.error(err);
        res.status(500).send('Erro ao deletar categoria');
    }
});

app.delete('/transactions/:id', async (req, res) => {
    const {id} = req.params;

    try {
        const query = 'DELETE FROM transactions WHERE id = ?';
        const [result] = await db.query(query, [id]);

        if (result.affectedRows === 0) {
            return res.status(404).send('Transação não encontrada');
        }
        res.json({message: 'Transação deletado com sucesso'});
    } catch (err) {
        console.error(err);
        res.status(500).send('Erro ao deletar transação');
    }
});

// --- NOVAS ROTAS PARA PERFIL ---

app.post('/profiles', async (req, res) => {
    const { user_id, income, housing_type, expenses_fixed, expenses_variable } = req.body;

    try {
        const query = 'INSERT INTO profiles (user_id, income, housing_type, expenses_fixed, expenses_variable) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE income=?, housing_type=?, expenses_fixed=?, expenses_variable=?';
        await db.query(query, [user_id, income, housing_type, expenses_fixed, expenses_variable, income, housing_type, expenses_fixed, expenses_variable]);

        res.status(201).json({ message: 'Perfil atualizado com sucesso!' });
    } catch (err) {
        console.error(err);
        res.status(500).send('Erro ao salvar perfil!');
    }
});

app.get('/profiles/:userId', async (req, res) => {
    const { userId } = req.params;

    try {
        const [rows] = await db.query('SELECT * FROM profiles WHERE user_id = ?', [userId]);
        if (rows.length === 0) {
            return res.status(404).send('Perfil não encontrado');
        }
        res.json(rows[0]);
    } catch (err) {
        console.error(err);
        res.status(500).send('Erro ao buscar perfil');
    }
});
