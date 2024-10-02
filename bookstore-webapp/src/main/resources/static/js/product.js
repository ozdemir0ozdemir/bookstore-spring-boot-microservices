document.addEventListener('alpine:init', () => {
    Alpine.data('initData', (pageNumber) => ({
        pageNumber: pageNumber,
        products: {
            data:[]
        },
        init(){
            this.loadProducts(this.pageNumber);
            updateCartItemCount();
        },
        loadProducts(pageNumber){
            $.getJSON("api/products?page="+pageNumber, (resp) => {
                console.log("Products Resp:", resp)
                this.products = resp;
            });
        },
        addToCart(product) {
            addProductToCart(product);
        },
    }));
});