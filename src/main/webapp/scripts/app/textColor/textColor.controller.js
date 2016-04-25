/**
 * Created by Amine on 12/24/15.
 */

(function () {
    angular.module('app')
        .controller('TextColorController', textColorController);
    textColorController.$injector = ['TextColor', 'SweetAlert', 'ParseLinks']
    function textColorController(TextColor, SweetAlert, ParseLinks) {

        var vm = this;
        vm.textColors = [];
        vm.alerts = [];
        vm.currentPage = 1;

        vm.loadAll = function () {
            TextColor.query({}, function (result, headers) {
                vm.textColors = result;
            });
        };

        vm.loadAll();







        vm.closeAlert = function (index) {
            vm.alerts.splice(index, 1);
        };



        vm.delete=function(i) {
            SweetAlert.swal({
                    title: "Are you sure?",
                    text: "The wont able to recover the TextColor: "+ i.tag +" ",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes, delete it"
                },
                function (isConfirm) {
                    if (isConfirm) {
                        TextColor.delete({id: i.id},
                            function () {
                                vm.loadAll();
                                vm.alerts.push({type: 'success', msg: 'TextColor Deleted'});

                            });

                    }
                });

        }

    }
})();
