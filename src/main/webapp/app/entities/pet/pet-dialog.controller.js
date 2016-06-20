(function () {
    'use strict';

    angular
        .module('petStoreApp')
        .controller('PetDialogController', PetDialogController);

    PetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pet', 'Tag', 'Category', 'PhotoUrl', '$log'];

    function PetDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Pet, Tag, Category, PhotoUrl, $log) {
        var vm = this;

        vm.pet = entity;
        vm.clear = clear;
        vm.save = save;
        vm.tags = Tag.query();
        vm.categories = Category.query();
        vm.photoUrls = PhotoUrl.query();
        vm.photoUrls = vm.photoUrls.join(',');

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.pet.photoUrls !== null) {
				if (typeof vm.pet.photoUrls === 'string' || vm.pet.photoUrls instanceof String) {
	                vm.pet.photoUrls = vm.pet.photoUrls.split(',');
				}
                // $log.log("PET: " + JSON.stringify(vm.pet));
            } else {
                vm.pet.photoUrls = [];
            }
            if (vm.pet.id !== null) {
                Pet.update(vm.pet, onSaveSuccess, onSaveError);
            } else {
                Pet.save(vm.pet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('petStoreApp:petUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }
    }
})();
