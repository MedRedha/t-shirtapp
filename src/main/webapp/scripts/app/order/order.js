'use strict';


angular.module('app')
    .config(function ($stateProvider) {
        $stateProvider
            .state('order', {
                url: '/orders',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: "scripts/app/order/order.html",
                        controller: "OrderController",
                        controllerAs: "vm"
                    }
                }



            }).state('order.create', {
            parent: "order",
            url: '/create',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/order/create.html',
                    controller: "OrderCreateController",
                    controllerAs: "vm"
                }
            }

        }).state('order.edit', {
                parent: "order",
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/order/update.html',
                        controller: "OrderCreateController",
                        controllerAs: "vm"
                    }
                }

            })
            .state('order.detail', {
            parent: "order",
            url: '/{id}',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/order/orderDialog.html',
                    controller: "OrderDialogController",
                    controllerAs: "vm"
                }
            }

        })

    });
    
    
    
    
