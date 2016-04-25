/**
 * Created by Amine on 12/24/15.
 */



'use strict';

     
angular.module('app')
    .config(function ($stateProvider) {
        $stateProvider
            .state('user', {
                abstract: true,
                parent: 'home'
            });
    });
