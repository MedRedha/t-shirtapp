/**
 * Created by Amine on 12/26/15.
 */


(function () {
    'use strict';
    angular.module("app")
        .controller("OrderCreateController", OrderCreateController);
    OrderCreateController.$inject = ['Order', '$stateParams', '$http', '$state', 'Client', 'User', 'Logo'];
    function OrderCreateController(Order, $stateParams, $http, $state, Client, User, Logo) {
        var vm = this;
        vm.order = {};
        vm.temp = {};
        vm.alerts = [];
        vm.type = {};
        vm.size = {};
        vm.product = {};
        vm.showSize = false;
        vm.showColor = false;
        vm.tempClients = [];
        vm.logo = {};
        vm.logoTemp = {};
        vm.logos = [];
        //charge the order if its an update
        if ($stateParams.id != null) {
            vm.loadOrder = function () {
                Order.get({id: $stateParams.id}, function (result) {
                    vm.order = result;
                    vm.showSize = true;
                    vm.showColor = true;
                });
            }
            vm.loadOrder()
        }
        //get all productTypes
        vm.productType = (function () {
            $http({
                method: 'GET',
                url: '/api/productType/status',
                params: {'status': 'ENABLED'}
            }).then(function successCallback(response) {
                vm.temp = response.data;
            }, function errorCallback(response) {
            });
            return vm.temp;
        })();
        //get all users
        vm.loadAllUsers = function () {
            User.query(function (result) {
                vm.tempUsers = result;
            });
        };
        vm.loadAllUsers();
        //save the new order or update
        vm.save = function () {
            vm.isSaving = true;
            if (vm.logoTemp != null) {
                vm.logo.uuid=vm.logoTemp.uuid;
                vm.logo.imagePath=vm.logoTemp.uuid;
                vm.logo.view=0;
                vm.logo.logo=true;
                vm.logo.tag=vm.logoTemp.tag;
                vm.logos.push(vm.logo)
                vm.order.imagerOrderList=vm.logos;
            }

            if (vm.order.id != null) {
                Order.update(vm.order, function (data, headers) {
                    $state.go('order');
                }, function (httpResponse) {
                    console.log(httpResponse.headers('failure'));
                    vm.logos=[];
                    vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});
                });
            } else {
                Order.save(vm.order, function (data, headers) {
                    $state.go('order');
                }, function (httpResponse) {
                    console.log(httpResponse.headers('failure'));
                    vm.logos=[];
                    vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});
                });
            }
        };
        //load all clients
        vm.loadClient = function () {
            Client.query(function (result, headers) {
                vm.tempClients = result;
            });
        };
        vm.loadClient();
        //load all logos
        vm.loadLogos = function () {
            Logo.query(function (result, headers) {
                vm.tempLogos = result;
            });
        };
        vm.loadLogos();
        //get the product avaliable sizes
        vm.productSize = function () {
            $http({
                method: 'GET',
                url: '/api/mobile/product',
                params: {'type': vm.type.id}
            }).then(function successCallback(response) {
                vm.tempSize = response.data;
                vm.showSize = true;
            }, function errorCallback(response) {
            });
            return vm.tempSize;
        };
        //get product colors
        vm.productColor = function () {
            $http({
                method: 'GET',
                url: '/api/mobile/product',
                params: {'type': vm.type.id, 'size': vm.size.size}
            }).then(function successCallback(response) {
                vm.tempColor = response.data;
                vm.showColor = true;
            }, function errorCallback(response) {
            });
            return vm.tempColor;
        };
        //close alert
        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };
    }
})();