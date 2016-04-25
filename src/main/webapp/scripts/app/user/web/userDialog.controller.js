/**
 * Created by Amine on 12/26/15.
 */


(function(){
    'use strict'
    angular.module("app")
        .controller("UserDialogController",userDialogController);

    userDialogController.$inject=['User', 'DateUtils','$stateParams','$state']

    function userDialogController(User, DateUtils,$stateParams,$state){

        var vm = this;
        vm.alerts = [];

        vm.user={}
        if($stateParams.id!=null) {
            vm.loadUser = function () {
                User.get({id: $stateParams.id}, function (result) {
                    vm.user = result;

                });
            }
            vm.loadUser()
        }
        vm.save = function () {
            vm.isSaving = true;
            if (vm.user.id != null) {
                User.update(vm.user, function (data, headers) {
                    $state.go('userweb');
                }, function (httpResponse) {
                    console.log(httpResponse.headers('failure'));
                    vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});
            });
            } else {
                User.save(vm.user, function (data, headers) {
                    $state.go('userweb');
                }, function (httpResponse) {
                    console.log(httpResponse.headers('failure'));
                    vm.alerts.push({type: 'danger', msg: '' + httpResponse.headers('failure')});
                });
            }
        };



        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };

    }

})();