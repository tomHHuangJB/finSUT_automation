import { useMemo } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { Alert, Box, Button, MenuItem, Paper, Stack, TextField, Typography } from '@mui/material';
import SaveIcon from '@mui/icons-material/Save';
import { apiErrorMessage, createOrder, listAccounts, listSecurities } from '../api/client';
import type { CreateOrderPayload, OrderSide, OrderType } from '../api/client';

type OrderForm = {
  externalOrderId: string;
  accountId: string;
  securityId: string;
  side: OrderSide;
  orderType: OrderType;
  quantity: number;
  limitPrice?: number;
};

export function CreateOrderPage() {
  const queryClient = useQueryClient();
  const accountsQuery = useQuery({ queryKey: ['accounts'], queryFn: listAccounts });
  const securitiesQuery = useQuery({ queryKey: ['securities'], queryFn: listSecurities });
  const activeAccounts = useMemo(() => (accountsQuery.data ?? []).filter((account) => account.status === 'ACTIVE'), [accountsQuery.data]);
  const activeSecurities = useMemo(() => (securitiesQuery.data ?? []).filter((security) => security.active), [securitiesQuery.data]);

  const { register, handleSubmit, reset, watch, formState: { errors } } = useForm<OrderForm>({
    defaultValues: {
      externalOrderId: `UI-ORDER-${Date.now()}`,
      side: 'BUY',
      orderType: 'MARKET',
      quantity: 100,
    },
  });

  const orderType = watch('orderType');

  const mutation = useMutation({
    mutationFn: createOrder,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] });
      reset({
        externalOrderId: `UI-ORDER-${Date.now()}`,
        side: 'BUY',
        orderType: 'MARKET',
        quantity: 100,
      });
    },
  });

  function submit(form: OrderForm) {
    const payload: CreateOrderPayload = {
      externalOrderId: form.externalOrderId.trim(),
      accountId: form.accountId,
      securityId: form.securityId,
      side: form.side,
      orderType: form.orderType,
      quantity: Number(form.quantity),
    };

    if (form.orderType === 'LIMIT') {
      payload.limitPrice = Number(form.limitPrice);
    }

    mutation.mutate(payload);
  }

  return (
    <Box sx={{ maxWidth: 760 }}>
      <Typography variant="h5" component="h1" gutterBottom>
        Create Order
      </Typography>
      <Paper variant="outlined" sx={{ p: 3 }}>
        <Stack component="form" spacing={2.5} onSubmit={handleSubmit(submit)}>
          {mutation.isSuccess && <Alert severity="success">Order created.</Alert>}
          {mutation.isError && <Alert severity="error">{apiErrorMessage(mutation.error)}</Alert>}

          <TextField
            label="External Order ID"
            error={Boolean(errors.externalOrderId)}
            helperText={errors.externalOrderId?.message}
            {...register('externalOrderId', { required: 'External order ID is required' })}
          />

          <TextField
            select
            label="Account"
            defaultValue=""
            error={Boolean(errors.accountId)}
            helperText={errors.accountId?.message}
            {...register('accountId', { required: 'Account is required' })}
          >
            {activeAccounts.map((account) => (
              <MenuItem key={account.id} value={account.id}>
                {account.accountCode} - {account.accountName}
              </MenuItem>
            ))}
          </TextField>

          <TextField
            select
            label="Security"
            defaultValue=""
            error={Boolean(errors.securityId)}
            helperText={errors.securityId?.message}
            {...register('securityId', { required: 'Security is required' })}
          >
            {activeSecurities.map((security) => (
              <MenuItem key={security.id} value={security.id}>
                {security.symbol} - {security.name}
              </MenuItem>
            ))}
          </TextField>

          <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: '1fr 1fr' }, gap: 2 }}>
            <TextField select label="Side" defaultValue="BUY" {...register('side')}>
              <MenuItem value="BUY">BUY</MenuItem>
              <MenuItem value="SELL">SELL</MenuItem>
            </TextField>
            <TextField select label="Order Type" defaultValue="MARKET" {...register('orderType')}>
              <MenuItem value="MARKET">MARKET</MenuItem>
              <MenuItem value="LIMIT">LIMIT</MenuItem>
            </TextField>
          </Box>

          <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: '1fr 1fr' }, gap: 2 }}>
            <TextField
              label="Quantity"
              type="number"
              slotProps={{ htmlInput: { min: 0.0001, step: 0.0001 } }}
              error={Boolean(errors.quantity)}
              helperText={errors.quantity?.message}
              {...register('quantity', { required: 'Quantity is required', min: { value: 0.0001, message: 'Quantity must be greater than zero' }, valueAsNumber: true })}
            />
            <TextField
              label="Limit Price"
              type="number"
              disabled={orderType !== 'LIMIT'}
              slotProps={{ htmlInput: { min: 0, step: 0.0001 } }}
              error={Boolean(errors.limitPrice)}
              helperText={orderType === 'LIMIT' ? errors.limitPrice?.message : 'Only used for limit orders'}
              {...register('limitPrice', {
                min: { value: 0, message: 'Limit price must be nonnegative' },
                valueAsNumber: true,
                validate: (value) => orderType !== 'LIMIT' || Number.isFinite(value) || 'Limit price is required for limit orders',
              })}
            />
          </Box>

          <Button type="submit" variant="contained" startIcon={<SaveIcon />} disabled={mutation.isPending || accountsQuery.isLoading || securitiesQuery.isLoading}>
            Create Order
          </Button>
        </Stack>
      </Paper>
    </Box>
  );
}
