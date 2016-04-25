'use strict';


angular.module('app')
    .config(function ($stateProvider) {
        $stateProvider
            .state('productType', {
                url: '/productTypes',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: "scripts/app/productType/productType.html",
                        controller: "ProductTypeController",
                        controllerAs: "vm",
                    }
                }

            }).state('productType.create', {
                parent: "productType",
                url: '/new',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Users'
            },

                views: {
                    'content@': {
                        templateUrl: 'scripts/app/productType/productTypeDialog.html',
                        controller: "ProductTypeDialogController",
                        controllerAs: "vm"
                    }
                }



            }).state('productType.edit', {
            parent: "productType",
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Users'
            },

            views: {
                'content@': {
                    templateUrl: 'scripts/app/productType/productTypeDialog.html',
                    controller: "ProductTypeDialogController",
                    controllerAs: "vm"
                }
            }

        });

    });
    
    
    
    
