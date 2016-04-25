'use strict';


angular.module('app')
    .config(function ($stateProvider) {
        $stateProvider
            .state('product', {
                url: '/products',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: "scripts/app/product/product.html",
                        controller: "ProductController",
                        controllerAs: "vm",
                    }
                }

            }).state('product.create', {
                parent: "product",
                url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },

                views: {
                    'content@': {
                        templateUrl: 'scripts/app/product/productDialog.html',
                        controller: "ProductDialogController",
                        controllerAs: "vm"
                    }
                }



            }).state('product.edit', {
            parent: "product",
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/product/productDialog.html',
                    controller: "ProductDialogController",
                    controllerAs: "vm"
                }
            }

        });

    });
    
    
    
    
