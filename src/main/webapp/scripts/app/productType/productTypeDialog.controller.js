/**
 * Created by Amine on 12/26/15.
 */


(function () {
    'use strict'
    angular.module("app")
        .controller("ProductTypeDialogController", productTypeDialogController);

    productTypeDialogController.$inject = ['ProductType', 'DateUtils', '$stateParams', '$state','Upload']

    function productTypeDialogController(ProductType, DateUtils, $stateParams, $state,Upload) {
        $.material.init();
        var vm = this;
        vm.alerts = [];


        vm.productType = {}
        if ($stateParams.id != null) {
            vm.loadProductType = function () {
                ProductType.get({id: $stateParams.id}, function (result) {
                    vm.productType = result;

                });
            }
            vm.loadProductType()
        }

        if (vm.productType.status == null) {
            vm.productType.status = "DISABLED";
        }
        vm.save = function () {
            vm.isSaving = true;

            if (vm.productType.logoPath == null) {
                console.log(vm.productType.logoPath);
                vm.alerts.push({type: 'danger', msg: 'Pls upload a logo'});
                return;
            }
            if (vm.productType.id != null) {
                ProductType.update(vm.productType, function (data, headers) {
                    $state.go('productType');
                }, function (httpResponse) {
                    console.log(httpResponse.headers('failure'));
                    vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});

                });
            } else {
                ProductType.save(vm.productType, function (data, headers) {
                    $state.go('productType');
                }, function (httpResponse) {
                    console.log(httpResponse.headers('failure'));
                    vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});

                });
            }
        };


        vm.submit = function () {
            if (vm.file) {
                vm.upload(vm.file);
            }
        };
        vm.upload = function (file) {
            Upload.upload({
                url: '/api/logo/upload',
                data: {file: file}
            }).then(function (resp) {
                    console.log('Success ' + resp.headers('X-tshirtApp-params'));
                    vm.productType.logoPath = resp.headers('X-tshirtApp-params');
                }
            )
        };


        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };


    }

})();