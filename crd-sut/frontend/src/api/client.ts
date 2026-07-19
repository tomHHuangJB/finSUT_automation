import axios from 'axios';

export type Account = {
  id: string;
  accountCode: string;
  accountName: string;
  accountType: string;
  baseCurrency: string;
  status: string;
  cashBalance: number;
  createdAt: string;
  updatedAt: string;
  version: number;
};

export type Security = {
  id: string;
  symbol: string;
  name: string;
  assetClass: string;
  currency: string;
  currentPrice: number;
  priceTimestamp: string;
  restricted: boolean;
  active: boolean;
  createdAt: string;
  updatedAt: string;
};

export type OrderSide = 'BUY' | 'SELL';
export type OrderType = 'MARKET' | 'LIMIT';
export type OrderStatus = 'DRAFT' | 'SUBMITTED' | 'APPROVED' | 'RELEASED' | 'PARTIALLY_FILLED' | 'FILLED' | 'CANCELED' | 'REJECTED';

export type Order = {
  id: string;
  externalOrderId: string;
  accountId: string;
  securityId: string;
  side: OrderSide;
  orderType: OrderType;
  quantity: number;
  limitPrice: number | null;
  filledQuantity: number;
  remainingQuantity: number;
  averageExecutionPrice: number | null;
  status: OrderStatus;
  createdBy: string;
  approvedBy: string | null;
  createdAt: string;
  approvedAt: string | null;
  releasedAt: string | null;
  completedAt: string | null;
  version: number;
};

export type CreateOrderPayload = {
  externalOrderId: string;
  accountId: string;
  securityId: string;
  side: OrderSide;
  orderType: OrderType;
  quantity: number;
  limitPrice?: number;
};

const api = axios.create({
  baseURL: '/api/v1',
  auth: {
    username: 'pm_user',
    password: 'password',
  },
});

export async function listAccounts() {
  const response = await api.get<Account[]>('/accounts');
  return response.data;
}

export async function listSecurities() {
  const response = await api.get<Security[]>('/securities');
  return response.data;
}

export async function listOrders() {
  const response = await api.get<Order[]>('/orders');
  return response.data;
}

export async function createOrder(payload: CreateOrderPayload) {
  const response = await api.post<Order>('/orders', payload, {
    headers: {
      'X-Correlation-Id': `ui-order-${Date.now()}`,
    },
  });
  return response.data;
}

export function apiErrorMessage(error: unknown) {
  if (axios.isAxiosError(error)) {
    const data = error.response?.data as { code?: string; message?: string; error?: string } | undefined;
    return data?.message ?? data?.code ?? data?.error ?? error.message;
  }
  return 'Unexpected error';
}
