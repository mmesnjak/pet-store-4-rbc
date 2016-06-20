(function() {
    'use strict';

    angular
        .module('petStoreApp')
        .controller('PhotoUrlDialogController', PhotoUrlDialogController);

    PhotoUrlDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PhotoUrl', 'Pet'];

    function PhotoUrlDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PhotoUrl, Pet) {
        var vm = this;

        vm.photoUrl = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pets = Pet.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.photoUrl.id !== null) {
                PhotoUrl.update(vm.photoUrl, onSaveSuccess, onSaveError);
            } else {
                PhotoUrl.save(vm.photoUrl, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('petStoreApp:photoUrlUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
