package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile.voucher

sealed interface VoucherAction {

    data object BackClicked : VoucherAction

    data object OrdersClicked : VoucherAction

    data object HomeClicked : VoucherAction

    data object ProfileClicked : VoucherAction
}