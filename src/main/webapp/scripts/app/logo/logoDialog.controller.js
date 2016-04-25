/**
 * Created by Amine on 12/26/15.
 */


(function(){
    'use strict';
    angular.module("app")
        .controller("LogoDialogController",logoDialogController);

    logoDialogController.$inject=['Logo','$stateParams','$http','$state','Upload','Category'];

    function logoDialogController(Logo,$stateParams,$http,$state,Upload,Category){

        var vm = this;

        $.material.init();


        vm.logo={};
        vm.temp={};
        vm.alerts=[];


        vm.loadCategory = function () {
            Category.query(function (result, headers) {
                vm.categorys = result;
            });
        };
        vm.loadCategory();


        if($stateParams.id!=null){
        vm.loadLogo=function(){
            Logo.get({id: $stateParams.id}, function(result) {
                vm.logo = result;

            });
        };
            vm.loadLogo();
        }


        if(vm.logo.status==null){
            vm.logo.status="DISABLED";
        }


        vm.save = function () {
            vm.isSaving = true;

            if (vm.logo.uuid == null) {
                vm.alerts.push({type: 'danger', msg: 'Pls upload a logo'});
                return;
            }
            if (vm.logo.id != null) {
                Logo.update(vm.logo,function(data,headers){
                    $state.go('logo');
                },function(httpResponse){
                    console.log(httpResponse.headers('failure'));
                    vm.alerts.push({ type: 'danger', msg: ''+httpResponse.headers('failure') });

                });
            } else {
                Logo.save(vm.logo,function(data,headers){
                    $state.go('logo');
                },function(httpResponse){
                    console.log(httpResponse.headers('failure'));
                    vm.alerts.push({ type: 'danger', msg: ''+httpResponse.headers('failure') });

                });

            }

        };




        vm.closeAlert = function(index) {
            vm.alerts.splice(index, 1);
        };


        vm.submit = function() {
            if (vm.file) {
                vm.upload(vm.file);
            }
        };
        vm.upload = function (file) {
            Upload.upload({
                url: '/api/logo/upload',
                data: {file: file}
            }).then(function (resp) {
                console.log('Success '+ resp.headers('X-tshirtApp-params'));
                vm.logo.uuid=resp.headers('X-tshirtApp-params');
            }
            )
        };

        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };


    }

})();