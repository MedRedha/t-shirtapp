/**
 * Created by Amine on 12/26/15.
 */


(function(){
    'use strict';
    angular.module("app")
        .controller("ProductDialogController",productDialogController);

    productDialogController.$inject=['Product','$stateParams','$http','$state','Upload'];

    function productDialogController(Product,$stateParams,$http,$state,Upload){

        $.material.init();
        var vm = this;



        vm.product={};
        vm.temp={};
        vm.alerts=[];

        if($stateParams.id!=null){
        vm.loadProduct=function(){
            Product.get({id: $stateParams.id}, function(result) {
                vm.product = result;

            });
        };
            vm.loadProduct();
        }

        vm.productType=(function(){

            $http({
                method: 'GET',
                url: '/api/productType/status',
                params:{'status':'ENABLED'}
            }).then(function successCallback(response) {
                vm.temp=response.data;

            }, function errorCallback(response) {

            });
            return vm.temp;
        })();
        if(vm.product.status==null){
            vm.product.status="DISABLED";
        }


        vm.save = function () {
            vm.isSaving = true;
            if (vm.product.logoPath == null) {
                console.log(vm.productType.logoPath);
                vm.alerts.push({type: 'danger', msg: 'Pls upload a logo'});
                return;
            }
            if(vm.product.productType!=null){
            if (vm.product.id != null) {
                Product.update(vm.product,function(data,headers){
                    $state.go('product');
                },function(httpResponse){
                    console.log(httpResponse.headers('failure'));
                    vm.alerts.push({ type: 'danger', msg: ''+httpResponse.headers('failure') });

                });
            } else {
                Product.save(vm.product,function(data,headers){
                    $state.go('product');
                },function(httpResponse){
                    console.log(httpResponse.headers('failure'));
                    vm.alerts.push({ type: 'danger', msg: ''+httpResponse.headers('failure') });

                });

            }
            }else {
                vm.alerts.push({ type: 'danger', msg: 'ProductTuype must be not empty' });
            }
        };




        vm.submit = function () {
            if (vm.file) {
                vm.upload(vm.file);
            }

        };

        vm.submitBack = function () {
            if (vm.fileBack) {
                vm.uploadBack(vm.fileBack);
            }

        };vm.submitLeft = function () {
            if (vm.fileLeft) {
                vm.uploadLeft(vm.fileLeft);
            }

        };

        vm.submitRight = function () {
            if (vm.fileRight) {
                vm.uploadRight(vm.fileRight);
            }

        };
        vm.upload = function (file) {
            Upload.upload({
                url: '/api/logo/upload',
                data: {file: file}
            }).then(function (resp) {
                    console.log('Success ' + resp.headers('X-tshirtApp-params'));
                    vm.product.logoPath = resp.headers('X-tshirtApp-params');
                }
            )
        };

        vm.uploadBack = function (file) {
            Upload.upload({
                url: '/api/logo/upload',
                data: {file: file}
            }).then(function (resp) {
                    console.log('Success ' + resp.headers('X-tshirtApp-params'));
                    vm.product.logoPathBack = resp.headers('X-tshirtApp-params');
                }
            )
        };


        vm.uploadLeft = function (file) {
            Upload.upload({
                url: '/api/logo/upload',
                data: {file: file}
            }).then(function (resp) {
                    console.log('Success ' + resp.headers('X-tshirtApp-params'));
                    vm.product.logoPathLeft = resp.headers('X-tshirtApp-params');
                }
            )
        };

        vm.uploadRight = function (file) {
            Upload.upload({
                url: '/api/logo/upload',
                data: {file: file}
            }).then(function (resp) {
                    console.log('Success ' + resp.headers('X-tshirtApp-params'));
                    vm.product.logoPathRight = resp.headers('X-tshirtApp-params');
                }
            )
        };


        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };

    }

})();