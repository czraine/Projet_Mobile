// server.js
const express = require('express');
const stripe = require('stripe')('sk_test_51QAgm4KP2IKvZV2SjUq2UHWTCvLLazaRgWDShpxVY6VVrj9o3cMAv3lOlrL525dgmU5SLHC4gMayXzUUUs6KZgFT00gn2E3U6H'); // Replace with your actual Stripe Secret Key
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const PORT = 3000;

// Middleware setup
app.use(bodyParser.json());
app.use(cors());

// Endpoint to list available products
app.get('/list-products', async (req, res) => {
    try {
        const products = await stripe.products.list();
        const prices = await stripe.prices.list();

        // Combine products and prices by matching product IDs
        const productList = products.data.map(product => {
            const price = prices.data.find(p => p.product === product.id);
            return {
                id: product.id,
                name: product.name,
                description: product.description,
                price: price ? price.unit_amount : null,
                currency: price ? price.currency : null,
                image: product.images && product.images.length > 0 ? product.images[0] : null, // Fetch the first image URL
            };
        });

        res.json(productList);
    } catch (error) {
        console.error('Error listing products:', error);
        res.status(500).json({ error: 'Failed to list products' });
    }
});


// Endpoint to create a PaymentIntent
app.post('/create-payment-intent', async (req, res) => {
    const { productId } = req.body;

    try {
        const product = await stripe.products.retrieve(productId);
        const price = await stripe.prices.list({ product: productId });

        if (!price.data[0]) throw new Error('Price not found for the product.');

        const paymentIntent = await stripe.paymentIntents.create({
            amount: price.data[0].unit_amount,
            currency: price.data[0].currency,
        });

        res.json({ clientSecret: paymentIntent.client_secret });
    } catch (error) {
        console.error('Error creating PaymentIntent:', error);
        res.status(500).json({ error: 'Failed to create PaymentIntent' });
    }
});

app.get('/hello', (req, res) => {
    res.send('Hello, World!');
});

// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});
