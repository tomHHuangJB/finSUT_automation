import { useQuery } from '@tanstack/react-query';
import { Alert, Box, Chip, CircularProgress, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@mui/material';
import { apiErrorMessage, listAccounts, listOrders, listSecurities } from '../api/client';

export function OrdersPage() {
  const ordersQuery = useQuery({ queryKey: ['orders'], queryFn: listOrders });
  const accountsQuery = useQuery({ queryKey: ['accounts'], queryFn: listAccounts });
  const securitiesQuery = useQuery({ queryKey: ['securities'], queryFn: listSecurities });

  if (ordersQuery.isLoading || accountsQuery.isLoading || securitiesQuery.isLoading) {
    return <CircularProgress aria-label="Loading orders" />;
  }

  const error = ordersQuery.error ?? accountsQuery.error ?? securitiesQuery.error;
  if (error) {
    return <Alert severity="error">Could not load orders: {apiErrorMessage(error)}</Alert>;
  }

  const accountById = new Map((accountsQuery.data ?? []).map((account) => [account.id, account.accountCode]));
  const securityById = new Map((securitiesQuery.data ?? []).map((security) => [security.id, security.symbol]));

  return (
    <Box>
      <Typography variant="h5" component="h1" gutterBottom>
        Order Blotter
      </Typography>
      <TableContainer component={Paper} variant="outlined">
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>External ID</TableCell>
              <TableCell>Account</TableCell>
              <TableCell>Security</TableCell>
              <TableCell>Side</TableCell>
              <TableCell>Type</TableCell>
              <TableCell align="right">Quantity</TableCell>
              <TableCell align="right">Remaining</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Created By</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {(ordersQuery.data ?? []).map((order) => (
              <TableRow key={order.id}>
                <TableCell>{order.externalOrderId}</TableCell>
                <TableCell>{accountById.get(order.accountId) ?? order.accountId}</TableCell>
                <TableCell>{securityById.get(order.securityId) ?? order.securityId}</TableCell>
                <TableCell>{order.side}</TableCell>
                <TableCell>{order.orderType}</TableCell>
                <TableCell align="right">{order.quantity}</TableCell>
                <TableCell align="right">{order.remainingQuantity}</TableCell>
                <TableCell>
                  <Chip size="small" label={order.status} color={order.status === 'DRAFT' ? 'info' : 'default'} />
                </TableCell>
                <TableCell>{order.createdBy}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}
