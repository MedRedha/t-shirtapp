/**
 * Created by Amine on 12/26/15.
 */


(function(){
    'use strict';
    angular.module("app")
        .controller("TextColorDialogController",textColorDialogController);

    textColorDialogController.$inject=['TextColor','$stateParams','$http','$state','Upload','Category'];

    function textColorDialogController(TextColor,$stateParams,$http,$state,Upload,Category){

        var vm = this;

        $.material.init();


        vm.textColor={};
        vm.temp={};
        vm.alerts=[];





        if($stateParams.id!=null){
        vm.loadTextColor=function(){
            TextColor.get({id: $stateParams.id}, function(result) {
                vm.textColor = result;

            });
        };
            vm.loadTextColor();
        }





        vm.save = function () {
            vm.isSaving = true;
            if(vm.textColor.value == null ){
                vm.alerts.push({ type: 'danger', msg: 'Color must be not null '});
                return;
            }

            if (vm.textColor.id != null) {
                TextColor.update(vm.textColor,function(data,headers){
                    $state.go('textColor');
                },function(httpResponse){
                    console.log(httpResponse.headers('failure'));
                    vm.alerts.push({ type: 'danger', msg: ''+httpResponse.headers('failure') });

                });
            } else {
                TextColor.save(vm.textColor,function(data,headers){
                    $state.go('textColor');
                },function(httpResponse){
                    console.log(httpResponse.headers('failure'));
                    vm.alerts.push({ type: 'danger', msg: ''+httpResponse.headers('failure') });

                });

            }

        };




        vm.closeAlert = function(index) {
            vm.alerts.splice(index, 1);
        };



    }

})();