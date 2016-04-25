/**
 * Created by Amine on 12/22/15.
 */


(function () {
    'use strict';
    angular.module('app', ['ui.router','ngResource','ui.bootstrap', 'ngAnimate','oitozero.ngSweetAlert','LocalStorageModule','ngCacheBuster','ngFileUpload','angular-loading-bar','bootstrapLightbox'])


        .run(function ($rootScope, $location, $window, $http, $state,  Auth, Principal) {

            $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
                console.log("amine cange state");
                $rootScope.toState = toState;
                $rootScope.toStateParams = toStateParams;

                if (Principal.isIdentityResolved()) {
                    Auth.authorize();
                }

            });


        })
        .config(routConfig);


    routConfig.$inject = ['$stateProvider', '$urlRouterProvider','$resourceProvider','$httpProvider','httpRequestInterceptorCacheBusterProvider']
    function routConfig($stateProvider, $urlRouterProvider,$resourceProvider,$httpProvider,httpRequestInterceptorCacheBusterProvider) {

       $resourceProvider.defaults.stripTrailingSlashes = false;
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);

        $urlRouterProvider.otherwise("/");

        $stateProvider .state('site', {

            'abstract': true,
            views: {
                'navbar@': {
                    templateUrl: 'scripts/app/navbar/navbar.html',
                    controller: 'NavbarController'
                },
                'sidenavbar@': {
                    templateUrl: 'scripts/app/sidenavbar/sidenavbar.html',
                    controller: 'SideNavbarController'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ]}
        });

        $httpProvider.interceptors.push('authExpiredInterceptor');
    }
})();