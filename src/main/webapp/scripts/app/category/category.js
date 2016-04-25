'use strict';


angular.module('app')
    .config(function ($stateProvider) {
        $stateProvider
            .state('category', {
                url: '/categorys',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: "scripts/app/category/category.html",
                        controller: "CategoryController",
                        controllerAs: "vm",
                    }
                }

            }).state('category.create', {
                parent: "category",
                url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },

                views: {
                    'content@': {
                        templateUrl: 'scripts/app/category/categoryDialog.html',
                        controller: "CategoryDialogController",
                        controllerAs: "vm"
                    }
                }



            }).state('category.edit', {
            parent: "category",
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/category/categoryDialog.html',
                    controller: "CategoryDialogController",
                    controllerAs: "vm"
                }
            }

        });

    });
    
    
    
    
