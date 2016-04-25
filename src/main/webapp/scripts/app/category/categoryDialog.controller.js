/**
 * Created by Amine on 12/26/15.
 */


(function(){
    'use strict';
    angular.module("app")
        .controller("CategoryDialogController",categoryDialogController);

    categoryDialogController.$inject=['Category','$stateParams','$http','$state','Upload'];

    function categoryDialogController(Category,$stateParams,$http,$state,Upload){

        var vm = this;

        $.material.init();


        vm.category={};
        vm.temp={};
        vm.alerts=[];

        if($stateParams.id!=null){
        vm.loadCategory=function(){
            Category.get({id: $stateParams.id}, function(result) {
                vm.category = result;

            });
        };
            vm.loadCategory();
        }


        if(vm.category.status==null){
            vm.category.status="DISABLED";
        }


        vm.save = function () {
            vm.isSaving = true;

            if (vm.category.name == null) {
                vm.alerts.push({type: 'danger', msg: 'Pls chose a name'});
                return;
            }
            if (vm.category.id != null) {
                Category.update(vm.category,function(data,headers){
                    $state.go('category');
                },function(httpResponse){
                    console.log(httpResponse.headers('failure'));
                    vm.alerts.push({ type: 'danger', msg: ''+httpResponse.headers('failure') });

                });
            } else {
                Category.save(vm.category,function(data,headers){
                    $state.go('category');
                },function(httpResponse){
                    console.log(httpResponse.headers('failure'));
                    vm.alerts.push({ type: 'danger', msg: ''+httpResponse.headers('failure') });

                });

            }

        };




        vm.closeAlert = function(index) {
            vm.alerts.splice(index, 1);
        };




        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };


    }

})();